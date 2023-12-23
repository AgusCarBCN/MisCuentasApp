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
import carnerero.agustin.cuentaappandroid.databinding.FragmentNotificationsBinding
import carnerero.agustin.cuentaappandroid.utils.AlarmNotifications
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.util.Calendar


class NotificationsFragment : Fragment() {
    companion object {
        const val CHANEL_NOTIFICATIONS= "Notifications"

    }

    // Variable para manejar el View Binding
    private var _binding:FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

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
        var incomePerMonth=0.0
        val switchAlertLimit = binding.switchalertlimit
        val switchWeeklyReport = binding.switchweek
        val switchMonthlyReport = binding.switchmonth
        val switchAlertBalance = binding.switchalertbalance
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        // Obtener el estado de los switchs de las notificaciones
        val isCheckedSwitchAlertLimit =
            sharedPreferences.getBoolean(getString(R.string.switchlimit), false)
        val isCheckedSwitchWeek =
            sharedPreferences.getBoolean(getString(R.string.switchweek), false)
        val isCheckedSwitchMonth =
            sharedPreferences.getBoolean(getString(R.string.switchmonth), false)
        val isCheckedSwitchAlertBalance =
            sharedPreferences.getBoolean(getString(R.string.switchbalance), false)

        //Asigno el estado de los switchs
        switchAlertLimit.isChecked = isCheckedSwitchAlertLimit
        switchAlertBalance.isChecked = isCheckedSwitchAlertBalance
        switchWeeklyReport.isChecked = isCheckedSwitchWeek
        switchMonthlyReport.isChecked = isCheckedSwitchMonth

        val seekBar = binding.seekBar
        val seekBarBal=binding.seekBarBalance
        val percentTextView = binding.tvPercent
        val percentTextViewBal=binding.tvPercentbalance
        // Aplica la visibilidad de la barra de progreso y el TextView
        seekBar.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextView.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        seekBarBal.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextViewBal.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
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
                scheduleNotification(AlarmNotifications.ALARM_LIMIT_NOTIFICATION)
            }

        }

        switchAlertBalance.setOnCheckedChangeListener { _, isChecked ->
            //Guardo configuracion en sharedPreferences
            sharedPreferences.edit().putBoolean(getString(R.string.switchbalance), isChecked)
                .apply()
            // Muestra u oculta la barra de progreso según el estado del interruptor
            seekBarBal.visibility=if (isChecked) View.VISIBLE else View.GONE
            // Muestra u oculta el TextView según el estado del interruptor
            percentTextViewBal.visibility = if (isChecked) View.VISIBLE else View.GONE
            //Obtengo el valor del porcentaje seleccionado del seekbar

            if (isChecked) {
                scheduleNotification(AlarmNotifications.ALARM_BALANCE)
            }

        }
        switchWeeklyReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleNotification(AlarmNotifications.REPORT_WEEKLY)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchweek), isChecked).apply()
        }
        switchMonthlyReport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleNotification(AlarmNotifications.REPORT_MONTLY)
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchmonth), isChecked).apply()
        }


        // Asigna un listener de cambio de progreso a la barra de progreso
        // Dentro de tu clase o función donde estás manejando el SeekBar y el TextView
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el valor del progreso y muestra el porcentaje en el TextView
                percentTextView.text = "$progress%"
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
        val savedProgressBal=sharedPreferences.getInt("progressValueBal",0)
        seekBar.progress = savedProgress
        seekBarBal.progress=savedProgressBal
        percentTextView.text = "$savedProgress%"
        percentTextViewBal.text="$savedProgressBal%"

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
    private fun scheduleNotification(notificationType:Int){
        val intent=Intent(requireContext().applicationContext,AlarmNotifications::class.java)
        intent.putExtra("notificationType", notificationType)
        val pendingIntent=PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationType,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager: AlarmManager = context?.getSystemService()!!
        when {
            // If permission is granted, proceed with scheduling exact alarms.
            alarmManager.canScheduleExactAlarms() -> {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,Calendar.getInstance().timeInMillis+10000,pendingIntent)
            }
            else -> {

            }
        }

    }
    private fun scheduleNotification(notificationType: Int, repeatInterval: Int) {
        val intent = Intent(requireContext().applicationContext, AlarmNotifications::class.java)
        intent.putExtra("notificationType", notificationType)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationType,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = context?.getSystemService()!!

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, repeatInterval) // Avanza la fecha actual según el intervalo especificado

        // Configura la notificación para que se repita según el intervalo proporcionado
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * repeatInterval.toLong(), // Intervalo de repetición en días
            pendingIntent
        )
    }


}
