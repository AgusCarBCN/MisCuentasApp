package carnerero.agustin.cuentaappandroid

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.ActivityMainBinding
import carnerero.agustin.cuentaappandroid.interfaces.OnLocaleListener
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.AlarmNotifications
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.abs


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val CHANEL_NOTIFICATION = "NotificationChanel"

        const val INTERVAL_WEEKLY = 7
        const val INTERVAL_MONTHLY = 30
        const val INTERVAL_DAYLY=1
    }

    private val admin = DataBaseAppSingleton.getInstance(this)
    private val cuentaDao= CuentaDao(admin)
    private val movDao = MovimientoBancarioDAO(admin)
    private var savedProgressBal:Int=0
    private var savedProgress:Int=0
    private var year=Utils.getYear()
    private var month=Utils.getMonth()
    private var week=Utils.getWeek()
    private lateinit var cuentas:ArrayList<Cuenta>
    private lateinit var movimientos: ArrayList<MovimientoBancario>
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment: Fragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el diseño de la actividad utilizando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //Recupera valor de los switchCompact que activan las notificaciones
        val isCheckedSwitchAlertBalance =
            sharedPreferences.getBoolean(getString(R.string.switchbalance), false)
        val isCheckedSwitchDay =
            sharedPreferences.getBoolean(getString(R.string.switchday), false)
        val isCheckedSwitchWeek =
            sharedPreferences.getBoolean(getString(R.string.switchweek), false)
        val isCheckedSwitchMonth =
            sharedPreferences.getBoolean(getString(R.string.switchmonth), false)
        val isCheckedSwitchAlertLimit =
            sharedPreferences.getBoolean(getString(R.string.switchlimit), false)
        //Recupera el valor de los progress de los seekbar
        savedProgressBal = sharedPreferences.getInt("progressValueBal", 0)
        savedProgress=sharedPreferences.getInt("progressValue",0)
        //Recupera las cuentas
        cuentas= cuentaDao.listarTodasLasCuentas() as ArrayList<Cuenta>
        //Obtiene todos los movimientos bancarios
        movimientos = movDao.getAll()
        //Crea canal para las notificaciones
        createChannel(CHANEL_NOTIFICATION)

        //Envia notificaciones si los switch estan activados.
        if(isCheckedSwitchDay){
            val report = showDaylyReport(movimientos)
            scheduleNotification(AlarmNotifications.REPORT_DAYRY,
                INTERVAL_DAYLY,report)
        }
        if(isCheckedSwitchWeek){
            val report=showWeeklyReport(movimientos)
            scheduleNotification(AlarmNotifications.REPORT_WEEKLY,
                INTERVAL_WEEKLY,report)
        }
        if(isCheckedSwitchMonth){
            val report=showMonthlyReport(movimientos)
            scheduleNotification(AlarmNotifications.REPORT_MONTLY,
                INTERVAL_MONTHLY,report)
        }

        if(isCheckedSwitchAlertBalance){
            checkAndNotifyIfBalanceIsBellowLimit()
        }

        if(isCheckedSwitchAlertLimit){
            checkAndNotifyIfExpensesIsAboveLimit()
        }
        // Configurar la barra de herramientas (toolbar)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // Acceso al DrawerLayout
        drawer = binding.draverLayout
        // Configurar ActionBarDrawerToggle para manejar la apertura y cierre del DrawerLayout
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // Habilitar el botón de inicio en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        // Configurar el NavigationView y su escucha de eventos de selección
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Obtener el contenedor de fragmentos
        val fragmentContainer: FragmentContainerView = findViewById(R.id.fcv_main_container)


        // Determinar el fragmento a mostrar según la selección del usuario en el NavigationView
        when (item.itemId) {
            R.id.home -> fragment = LogoFragment()
            R.id.consulta -> fragment = ConsultaFragment()
            R.id.nuevoImporte -> fragment = NewAmountFragment()
            R.id.notification->fragment=NotificationsFragment()
            R.id.estadistica -> fragment = BarChartFragment()
            R.id.transferencia ->fragment = TransaccionFragment()
            R.id.db->fragment=DBFragment()
            R.id.configuracion -> fragment = AjustesFragment()
            R.id.about->fragment=AboutFragment()
            R.id.calculator->fragment = CalculatorFragment()
            R.id.profile->fragment=InfoFragment()
            R.id.salir -> {
                // Finalizar la actividad si se selecciona "salir"
                finish()
                return true
            }
            else -> fragment = ListOfAccountsFragment()
        }

        // Mostrar el fragmento de saldo en el contenedor de información
        showSaldo()
        // Cambiar el fragmento principal en el contenedor principal
        changeFragmentMain(fragment)
        // Establecer la visibilidad del contenedor de fragmentos
        fragmentContainer.visibility = View.VISIBLE
        // Cerrar el DrawerLayout después de la selección
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        // Sincronizar el estado de ActionBarDrawerToggle después de que se ha restaurado la instancia
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Manejar cambios en la configuración, por ejemplo, rotaciones de pantalla
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    private fun changeFragmentMain(fragment: Fragment) {
        // Cambiar el fragmento principal en el contenedor principal
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .commit()
    }

    private fun showSaldo() {
        // Mostrar el fragmento de saldo en el contenedor de información
        val fragmentSaldo = ListOfAccountsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.info_container, fragmentSaldo)
            .commit()
    }

    fun actualizarFragmentSaldo() {
        // Actualizar el fragmento de saldo en el contenedor de información
        val fragmentSaldo = ListOfAccountsFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.info_container, fragmentSaldo)
        transaction.commit()
    }

    fun inicio() {
        // Mostrar el fragmento de inicio en el contenedor principal
        val fragment = LogoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fcv_main_container, fragment)
        transaction.commit()
    }
    private fun checkAndNotifyIfBalanceIsBellowLimit() {

            val limit = savedProgressBal
            val stringBuilder = StringBuilder()
            stringBuilder.append(getString(R.string.lowbalance))
            stringBuilder.append(" $limit")
            // Obtengo saldos de cuentas
            for (cuenta: Cuenta in cuentas) {
                if (cuenta.saldo <= limit) {
                    stringBuilder.append(".${getString(R.string.account)}:${cuenta.iban}")
                    scheduleNotificationAlert(stringBuilder.toString(),AlarmNotifications.ALARM_BALANCE)
                }
            }
    }
    private fun checkAndNotifyIfExpensesIsAboveLimit() {

        val limit = savedProgress
        val stringBuilder = StringBuilder()

        stringBuilder.append(getString(R.string.expenseslimit))

        val gastos:ArrayList<MovimientoBancario> =ArrayList()
        for(mov in movimientos){
            if(mov.importe>=0){
                gastos.add(mov)
            }
        }
        val expensesMonth= Utils.calcularImporteMes(month,year,gastos).toDouble()
        val difExpensesLimit=abs(expensesMonth)-limit
        // Format the expense to two decimal places
        val formattedExpense = String.format("%.2f", difExpensesLimit)

        stringBuilder.append(" $formattedExpense")
        if(expensesMonth>=limit){
            scheduleNotificationAlert(stringBuilder.toString(),AlarmNotifications.ALARM_LIMIT_NOTIFICATION)
        }
    }


    private fun scheduleNotificationAlert(report: String,notificationType: Int) {

        val intent = Intent(applicationContext, AlarmNotifications::class.java)
        intent.putExtra("notificationType", notificationType)
        intent.putExtra("message", report)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationType,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager: AlarmManager = applicationContext?.getSystemService()!!
        // Configurar la notificación para que se dispare inmediatamente
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            pendingIntent
        )

    }
    private fun scheduleNotification(notificationType: Int, intervalDay: Int, report: String) {
        val intent = Intent(applicationContext, AlarmNotifications::class.java)
        intent.putExtra("notificationType", notificationType)
        intent.putExtra("message", report)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationType,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = applicationContext?.getSystemService()!!

        // Calcular la hora de inicio para la notificación (ajusta la hora y el minuto según tus necesidades)
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 22) //
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.SECOND, 0)

        when (intervalDay) {
            INTERVAL_DAYLY -> {
                // Configurar la notificación para que se repita diariamente a la misma hora
                val intervalMillis = AlarmManager.INTERVAL_DAY
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    startTime.timeInMillis,
                    intervalMillis,
                    pendingIntent
                )
            }
            INTERVAL_WEEKLY -> {
                // Configurar la notificación para que se repita cada semana a la misma hora (domingo)
                startTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

                // Verificar si el día actual es después del domingo, si es así, ajustar para la próxima semana
                val today = Calendar.getInstance()
                if (today.after(startTime)) {
                    startTime.add(Calendar.WEEK_OF_YEAR, 1)
                }

                val intervalMillis = AlarmManager.INTERVAL_DAY * 7
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    startTime.timeInMillis,
                    intervalMillis,
                    pendingIntent
                )
            }
            INTERVAL_MONTHLY -> {
                // Configurar la notificación para que se repita al final de cada mes
                startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH))

                // Verificar si el día actual es después del último día del mes, si es así, ajustar para el próximo mes
                val today = Calendar.getInstance()
                if (today.after(startTime)) {
                    startTime.add(Calendar.MONTH, 1)
                }

                val intervalMillis = AlarmManager.INTERVAL_DAY * startTime.getActualMaximum(Calendar.DAY_OF_MONTH)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    startTime.timeInMillis,
                    intervalMillis,
                    pendingIntent
                )
            }
        }
    }
    private fun createChannel(typeChannel:String) {
        val channel = NotificationChannel(
            typeChannel,
            "channelAlert",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
    private fun showDaylyReport(movimientos: ArrayList<MovimientoBancario>): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val stringBuilder = StringBuilder()
        for (movimiento in movimientos) {
            val fechaImporteDate = LocalDate.parse(movimiento.fechaImporte, formatter)
            val today= LocalDate.now()
            if (fechaImporteDate.isEqual(today)) {
                stringBuilder.append(" ${movimiento.importe}  ${movimiento.descripcion}  ${movimiento.iban}\n")
            }
        }
        if(stringBuilder.isEmpty()){
            stringBuilder.append(getString(R.string.nomovtoday))
        }
        return stringBuilder.toString()
    }
    private fun showWeeklyReport(movimientos: ArrayList<MovimientoBancario>):String{
        val stringBuilder = StringBuilder()
        week= Utils.getWeek()
        year= Utils.getYear()

        val ingresos:ArrayList<MovimientoBancario> =ArrayList()
        val gastos:ArrayList<MovimientoBancario> =ArrayList()
        for(mov in movimientos){
            if(mov.importe>=0){
                ingresos.add(mov)
            }else{
                gastos.add(mov)
            }
        }
        val ingresosSemana= Utils.calcularImporteSemanal(week,year,ingresos).toDouble()
        val gastosSemana= Utils.calcularImporteSemanal(week,year,gastos).toDouble()
        val result=ingresosSemana-gastosSemana
        stringBuilder.append("${getString(R.string.weekicome)}: ${ingresosSemana}\n")
        stringBuilder.append("${getString(R.string.weekbills)}: ${gastosSemana}\n")
        stringBuilder.append("${getString(R.string.resul)}: $result")
        return stringBuilder.toString()
    }
    private fun showMonthlyReport(movimientos: ArrayList<MovimientoBancario>):String{
        val stringBuilder = StringBuilder()
        month= Utils.getMonth()
        year= Utils.getYear()
        val ingresos:ArrayList<MovimientoBancario> =ArrayList()
        val gastos:ArrayList<MovimientoBancario> =ArrayList()
        for(mov in movimientos){
            if(mov.importe>=0){
                ingresos.add(mov)
            }else{
                gastos.add(mov)
            }
        }
        val ingresosMes= Utils.calcularImporteMes(month,year,ingresos).toDouble()
        val gastosMes= Utils.calcularImporteMes(month,year,gastos).toDouble()
        val result=ingresosMes-gastosMes
        stringBuilder.append("${getString(R.string.monthicome)}: ${ingresosMes}\n")
        stringBuilder.append("${getString(R.string.monthbills)}: ${gastosMes}\n")
        stringBuilder.append("${getString(R.string.resul)}: $result")
        return stringBuilder.toString()
    }
}
