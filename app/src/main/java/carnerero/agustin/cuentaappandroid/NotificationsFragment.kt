package carnerero.agustin.cuentaappandroid



import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentNotificationsBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.AlarmNotifications
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Calendar



class NotificationsFragment : Fragment() {
    companion object {
        const val CHANEL_NOTIFICATIONS = "Notifications"
        const val INTERVAL_WEEKLY = 7
        const val INTERVAL_MONTHLY = 30
        const val INTERVAL_DAYLY=1
    }

    // Variable para manejar el View Binding
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var movimientos: ArrayList<MovimientoBancario>
    private lateinit var cuentas:ArrayList<Cuenta>
    private var gastosMes=0.0
    private var ingresosMes=0.0
    private var year=Utils.getYear()
    private var month=Utils.getMonth()
    private var week=Utils.getWeek()
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val movDao = MovimientoBancarioDAO(admin)
    private val cuentaDao=CuentaDao(admin)
    private var savedProgress:Int=0
    private var savedProgressBal:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val switchDiaryReport = binding.switchday
        val switchWeeklyReport = binding.switchweek
        val switchMonthlyReport = binding.switchmonth
        val switchAlertBalance = binding.switchalertbalance
        val switchAlertLimit = binding.switchalertlimit
        val seekBar = binding.seekBar
        val seekBarBal = binding.seekBarBalance
        val percentTextView = binding.tvPercent
        val percentTextViewBal = binding.tvPercentbalance


        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        // Obtener el estado de los switchs de las notificaciones
        val isCheckedSwitchDay =
            sharedPreferences.getBoolean(getString(R.string.switchday), false)
        val isCheckedSwitchWeek =
            sharedPreferences.getBoolean(getString(R.string.switchweek), false)
        val isCheckedSwitchMonth =
            sharedPreferences.getBoolean(getString(R.string.switchmonth), false)
        val isCheckedSwitchAlertBalance =
            sharedPreferences.getBoolean(getString(R.string.switchbalance), false)
        val isCheckedSwitchAlertLimit =
            sharedPreferences.getBoolean(getString(R.string.switchlimit), false)
        //Recupera el progreso de SharedPreferences cuando se inicia tu actividad o fragmento

         savedProgressBal = sharedPreferences.getInt("progressValueBal", 0)
         savedProgress=sharedPreferences.getInt("progressValue",0)
        //Recupera las cuentas
        cuentas= cuentaDao.listarTodasLasCuentas() as ArrayList<Cuenta>
        //Asigno el estado de los switchs
        switchDiaryReport.isChecked = isCheckedSwitchDay
        switchAlertLimit.isChecked = isCheckedSwitchAlertLimit
        switchAlertBalance.isChecked = isCheckedSwitchAlertBalance
        switchWeeklyReport.isChecked = isCheckedSwitchWeek
        switchMonthlyReport.isChecked = isCheckedSwitchMonth

        // Aplica la visibilidad de la barra de progreso y el TextView
        seekBar.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextView.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        seekBarBal.visibility = if (isCheckedSwitchAlertBalance) View.VISIBLE else View.GONE
        percentTextViewBal.visibility = if (isCheckedSwitchAlertBalance) View.VISIBLE else View.GONE

        movimientos = movDao.getAll()

        //canal de notificaciones
        createChannel()
        checkAndNotifyIfBelowLimit()
        //switch de alerta de gastos permisibles
        switchAlertLimit.setOnCheckedChangeListener { _, isChecked ->
            //Guardo configuracion en sharedPreferences
            sharedPreferences.edit().putBoolean(getString(R.string.switchlimit), isChecked).apply()
            // Muestra u oculta la barra de progreso según el estado del interruptor
            seekBar.visibility = if (isChecked) View.VISIBLE else View.GONE
            // Muestra u oculta el TextView según el estado del interruptor
            percentTextView.visibility = if (isChecked) View.VISIBLE else View.GONE
            //Obtengo el valor del porcentaje seleccionado del seekbar
            if (isChecked) {
                //scheduleNotification(AlarmNotifications.ALARM_LIMIT_NOTIFICATION)
            }
        }

        switchAlertBalance.setOnCheckedChangeListener { _, isChecked ->
            //Guardo configuracion en sharedPreferences
            sharedPreferences.edit().putBoolean(getString(R.string.switchbalance), isChecked)
                .apply()
            var limit=savedProgressBal

            // Muestra u oculta la barra de progreso según el estado del interruptor
            seekBarBal.visibility = if (isChecked) View.VISIBLE else View.GONE
            // Muestra u oculta el TextView según el estado del interruptor
            percentTextViewBal.visibility = if (isChecked) View.VISIBLE else View.GONE

            //Obtengo saldos de cuentas
            if (isChecked) {
                checkAndNotifyIfBelowLimit()

            }
        }
        switchDiaryReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val report = showDaylyReport(movimientos)
                scheduleNotification(AlarmNotifications.REPORT_DAYRY, INTERVAL_DAYLY,report)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchday), isChecked).apply()
        }

        switchWeeklyReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val report=showWeeklyReport(movimientos)
                scheduleNotification(AlarmNotifications.REPORT_WEEKLY, INTERVAL_WEEKLY,report)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchweek), isChecked).apply()
        }
        switchMonthlyReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val report=showMonthlyReport(movimientos)
                scheduleNotification(AlarmNotifications.REPORT_MONTLY, INTERVAL_MONTHLY,report)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchmonth), isChecked).apply()
        }


        // Asigna un listener de cambio de progreso a la barra de progreso
        // Dentro de tu clase o función donde estás manejando el SeekBar y el TextView
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el valor del progreso y muestra el porcentaje en el TextView
                percentTextView.text = "${progress}%"
                // Guarda el progreso en SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putInt("progressValue", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
            }
        })

        seekBarBal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el valor del progreso y muestra el porcentaje en el TextView
                percentTextViewBal.text = "$progress"
                // Guarda el progreso en SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putInt("progressValueBal", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
            }
        })

        //Recupera el progreso de SharedPreferences cuando se inicia tu actividad o fragmento
         savedProgress = sharedPreferences.getInt("progressValue", 0)
         savedProgressBal = sharedPreferences.getInt("progressValueBal", 0)
        seekBar.progress = savedProgress
        seekBarBal.progress = savedProgressBal
        percentTextView.text = "$savedProgress%"
        percentTextViewBal.text = "$savedProgressBal"
        return binding.root
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANEL_NOTIFICATIONS,
            "channelAlert",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager: NotificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleNotification(notificationType: Int, intervalDay: Int, report: String) {
        val intent = Intent(requireContext().applicationContext, AlarmNotifications::class.java)
        intent.putExtra("notificationType", notificationType)
        intent.putExtra("message", report)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationType,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = context?.getSystemService()!!

        // Calcular la hora de inicio para la notificación (ajusta la hora y el minuto según tus necesidades)
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 18) //
        startTime.set(Calendar.MINUTE, 55)
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
    private fun scheduleNotificationBalance(notificationType: Int, report: String) {

            val intent = Intent(requireContext().applicationContext, AlarmNotifications::class.java)
            intent.putExtra("notificationType", notificationType)
            intent.putExtra("message", report)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext().applicationContext,
                notificationType,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager: AlarmManager = context?.getSystemService()!!
            // Configurar la notificación para que se dispare inmediatamente
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                pendingIntent
            )
    }

    private fun scheduleNotificationv1(notificationType: Int,intervalDay:Int,report:String) {
        //val str = showDaylyReport(movimientos)
        val intent = Intent(requireContext().applicationContext, AlarmNotifications::class.java)
        intent.putExtra("notificationType", notificationType)
        intent.putExtra("message", report)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationType,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = context?.getSystemService()!!

        // Calcular la hora de inicio para la notificación (ajusta la hora y el minuto según tus necesidades)
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 21) // Hora de inicio (ejemplo: 17:00PM)
        startTime.set(Calendar.MINUTE, 35)
        startTime.set(Calendar.SECOND, 0)
        val intervalMillis = AlarmManager.INTERVAL_DAY*intervalDay
        // Configurar la notificación para que se repita cada día a la misma hora
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime.timeInMillis,
            intervalMillis,
            pendingIntent
        )
    }
    private fun showDaylyReport(movimientos: ArrayList<MovimientoBancario>): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val stringBuilder = StringBuilder()
        for (movimiento in movimientos) {
            val fechaImporteDate = LocalDate.parse(movimiento.fechaImporte, formatter)
            val today=LocalDate.now()
            if (fechaImporteDate.isEqual(today)) {
                val fechaFormateada = fechaImporteDate.format(formatter)
                stringBuilder.append(" ${movimiento.importe}  ${movimiento.descripcion}  ${movimiento.iban}\n")
            }
        }
        if(stringBuilder.isNullOrEmpty()){
            stringBuilder.append(getString(R.string.nomovtoday))
        }
        return stringBuilder.toString()
    }
    private fun showWeeklyReport(movimientos: ArrayList<MovimientoBancario>):String{
        val stringBuilder = StringBuilder()
        week=Utils.getWeek()
        year= Utils.getYear()
        var ingresosSemana=0.0
        var gastosSemana=0.0
        var result=0.0
        val ingresos:ArrayList<MovimientoBancario> =ArrayList()
        val gastos:ArrayList<MovimientoBancario> =ArrayList()
        for(mov in movimientos){
            if(mov.importe>=0){
                ingresos.add(mov)
            }else{
                gastos.add(mov)
            }
        }
        ingresosSemana= Utils.calcularImporteSemanal(week,year,ingresos).toDouble()
        gastosSemana=Utils.calcularImporteSemanal(week,year,gastos).toDouble()
        result=ingresosSemana-gastosSemana
        stringBuilder.append("${getString(R.string.weekicome)}: ${ingresosSemana}\n")
        stringBuilder.append("${getString(R.string.weekbills)}: ${gastosSemana}\n")
        stringBuilder.append("${getString(R.string.resul)}: ${result}")
        return stringBuilder.toString()
    }
    private fun showMonthlyReport(movimientos: ArrayList<MovimientoBancario>):String{
        val stringBuilder = StringBuilder()
        month=Utils.getMonth()
        year= Utils.getYear()
        var ingresosMes=0.0
        var gastosMes=0.0
        var result=0.0
        val ingresos:ArrayList<MovimientoBancario> =ArrayList()
        val gastos:ArrayList<MovimientoBancario> =ArrayList()
        for(mov in movimientos){
            if(mov.importe>=0){
                ingresos.add(mov)
            }else{
                gastos.add(mov)
            }
        }
        ingresosMes= Utils.calcularImporteMes(month,year,ingresos).toDouble()
        gastosMes=Utils.calcularImporteMes(month,year,gastos).toDouble()
        result=ingresosMes-gastosMes
        stringBuilder.append("${getString(R.string.monthicome)}: ${ingresosMes}\n")
        stringBuilder.append("${getString(R.string.monthbills)}: ${gastosMes}\n")
        stringBuilder.append("${getString(R.string.resul)}: ${result}")
        return stringBuilder.toString()
    }
    fun checkAndNotifyIfBelowLimit() {
        // Obtén el estado actual del interruptor desde SharedPreferences
        val isChecked = sharedPreferences.getBoolean(getString(R.string.switchbalance), false)

        // Si el interruptor está activado, realiza la lógica de notificación
        if (isChecked) {
            val limit = savedProgressBal
            val stringBuilder = StringBuilder()
            stringBuilder.append(getString(R.string.lowbalance))
            stringBuilder.append(" $limit")

            // Obtengo saldos de cuentas
            for (cuenta: Cuenta in cuentas) {
                if (cuenta.saldo <= limit) {
                    stringBuilder.append(".${getString(R.string.account)}:${cuenta.iban}")
                    scheduleNotificationBalance(AlarmNotifications.ALARM_BALANCE, stringBuilder.toString())
                }
            }
        }
    }

}