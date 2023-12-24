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
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentNotificationsBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.AlarmNotifications
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar



class NotificationsFragment : Fragment() {
    companion object {
        const val CHANEL_NOTIFICATIONS = "Notifications"

    }

    // Variable para manejar el View Binding
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var movimientos: ArrayList<MovimientoBancario>
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val movDao = MovimientoBancarioDAO(admin)


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
        //Asigno el estado de los switchs
        switchDiaryReport.isChecked = isCheckedSwitchDay
        switchAlertLimit.isChecked = isCheckedSwitchAlertLimit
        switchAlertBalance.isChecked = isCheckedSwitchAlertBalance
        switchWeeklyReport.isChecked = isCheckedSwitchWeek
        switchMonthlyReport.isChecked = isCheckedSwitchMonth


        // Aplica la visibilidad de la barra de progreso y el TextView
        seekBar.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextView.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        seekBarBal.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextViewBal.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        //Ingresos y gastos..prueba para BBVA

        movimientos = movDao.getAll()


        //canal de notificaciones
        createChannel()
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
            // Muestra u oculta la barra de progreso según el estado del interruptor
            seekBarBal.visibility = if (isChecked) View.VISIBLE else View.GONE
            // Muestra u oculta el TextView según el estado del interruptor
            percentTextViewBal.visibility = if (isChecked) View.VISIBLE else View.GONE


            //Obtengo saldos de cuentas
            if (isChecked) {
                //scheduleNotification(AlarmNotifications.ALARM_BALANCE)
            }

        }
        switchDiaryReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val report = showDaylyReport(movimientos)
                scheduleNotification(AlarmNotifications.REPORT_DAYRY,+1,report)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchday), isChecked).apply()
        }

        switchWeeklyReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val report=showWeeklyReport(movimientos)
                scheduleNotification(AlarmNotifications.REPORT_WEEKLY,7,report)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchweek), isChecked).apply()
        }
        switchMonthlyReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val report=showMonthlyReport(movimientos)
                scheduleNotification(AlarmNotifications.REPORT_MONTLY,30,report)
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
                percentTextViewBal.text = "$progress%"
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
        val savedProgress = sharedPreferences.getInt("progressValue", 0)
        val savedProgressBal = sharedPreferences.getInt("progressValueBal", 0)
        seekBar.progress = savedProgress
        seekBarBal.progress = savedProgressBal
        percentTextView.text = "$savedProgress%"
        percentTextViewBal.text = "$savedProgressBal%"

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



    private fun scheduleNotification(notificationType: Int,intervalDay:Int,report:String) {
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
    fun showDaylyReport(movimientos: ArrayList<MovimientoBancario>): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val stringBuilder = StringBuilder()
        for (movimiento in movimientos) {
            val fechaImporteDate = LocalDate.parse(movimiento.fechaImporte, formatter)
            val fechaHoy = LocalDate.now()
            if (fechaImporteDate.isEqual(fechaHoy)) {
                val fechaFormateada = fechaImporteDate.format(formatter)
                stringBuilder.append(" ${movimiento.importe}  ${movimiento.descripcion}  ${movimiento.iban}\n")
            }
        }
        if(stringBuilder.isNullOrEmpty()){
            stringBuilder.append(getString(R.string.nomovtoday))
        }
        return stringBuilder.toString()
    }
    fun showWeeklyReport(movimientos: ArrayList<MovimientoBancario>):String{
        val stringBuilder = StringBuilder()
        val week=Utils.getWeek()
        val year= Utils.getYear()
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
    fun showMonthlyReport(movimientos: ArrayList<MovimientoBancario>):String{
        val stringBuilder = StringBuilder()
        val month=Utils.getMonth()
        val year= Utils.getYear()
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

}