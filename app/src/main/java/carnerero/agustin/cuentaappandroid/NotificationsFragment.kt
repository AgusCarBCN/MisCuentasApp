package carnerero.agustin.cuentaappandroid


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentNotificationsBinding



class NotificationsFragment : Fragment() {
    companion object {
        const val CHANEL_ALERT_LIMIT = "Spending limit alert"
        const val CHANEL_ALERT_BALANCE = "Balance Alert"
        const val CHANEL_WEEKLY_REPORT = "Weekly report"
        const val CHANEL_MONTLY_REPORT = "Monthly report"
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
        val percentTextView = binding.tvPercent
        // Aplica la visibilidad de la barra de progreso y el TextView
        seekBar.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextView.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        createChannel()
        switchAlertLimit.setOnCheckedChangeListener { _, isChecked ->

            //Guardo configuracion en sharedPreferences
            sharedPreferences.edit().putBoolean(getString(R.string.switchlimit), isChecked).apply()
            // Muestra u oculta la barra de progreso según el estado del interruptor
            seekBar.visibility = if (isChecked) View.VISIBLE else View.GONE
            // Muestra u oculta el TextView según el estado del interruptor
            percentTextView.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (isChecked) {
                createNotification()
            }
        }
        createChannel()
        switchAlertBalance.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                createNotification()
            }
            sharedPreferences.edit().putBoolean(getString(R.string.switchbalance), isChecked)
                .apply()
        }
        switchWeeklyReport.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(getString(R.string.switchweek), isChecked).apply()
        }
        switchMonthlyReport.setOnCheckedChangeListener { _, isChecked ->
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
        //Recupera el progreso de SharedPreferences cuando se inicia tu actividad o fragmento
        val savedProgress = sharedPreferences.getInt("progressValue", 0)
        seekBar.progress = savedProgress
        percentTextView.text = "$savedProgress%"

        return binding.root

    }

   private fun createNotification() {
        val intent= Intent(requireContext(),MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag=PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(requireContext(),
            0, intent,
             flag)
        // Crear un NotificationCompat.Builder
        val builder = NotificationCompat.Builder(requireContext(), CHANEL_ALERT_LIMIT)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Alerta de gastos")
            .setContentText("Esto es una prueba")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(requireContext())){

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(1,builder.build())
        }
    }
    private fun createChannel() {
            val channel = NotificationChannel(
                CHANEL_ALERT_LIMIT,
                "channelAlert",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
    }
}
