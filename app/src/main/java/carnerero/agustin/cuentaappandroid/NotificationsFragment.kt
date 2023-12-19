package carnerero.agustin.cuentaappandroid

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
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
import carnerero.agustin.cuentaappandroid.utils.Utils
import okhttp3.internal.notify


class NotificationsFragment : Fragment() {
    companion object {
        const val CHANEL_ALERT_LIMIT = "Spending limit alert"
        const val CHANEL_ALERT_BALANCE = "Balance Alert"
        const val CHANEL_WEEKLY_REPORT = "Weekly report"
        const val CHANEL_MONTLY_REPORT = "Monthly report"
    }

    // Variable para manejar el View Binding
    private var _binding: FragmentNotificationsBinding? = null
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
        var progressValue = 0
        var switchAlertLimit = binding.switchalertlimit
        var switchWeeklyReport = binding.switchweek
        var switchMonthlyReport = binding.switchmonth
        var switchAlertBalance = binding.switchalertbalance
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        // Obtener el estado de los switchs de las notificaciones
        var isCheckedSwitchAlertLimit =
            sharedPreferences.getBoolean(getString(R.string.switchlimit), false)
        var isCheckedSwitchWeek =
            sharedPreferences.getBoolean(getString(R.string.switchweek), false)
        var isCheckedSwitchMonth =
            sharedPreferences.getBoolean(getString(R.string.switchmonth), false)
        var isCheckedSwitchAlertBalance =
            sharedPreferences.getBoolean(getString(R.string.switchbalance), false)

        //Asigno el estado de los switchs
        switchAlertLimit.isChecked = isCheckedSwitchAlertLimit
        switchAlertBalance.isChecked = isCheckedSwitchAlertBalance
        switchWeeklyReport.isChecked = isCheckedSwitchWeek
        switchMonthlyReport.isChecked = isCheckedSwitchMonth

        val seekBar = binding.seekBar
        var percentTextView = binding.tvPercent
        // Aplica la visibilidad de la barra de progreso y el TextView
        seekBar?.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
        percentTextView?.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE

        switchAlertLimit.setOnCheckedChangeListener { buttonView, isChecked ->

            //Guardo configuracion en sharedPreferences
            sharedPreferences.edit().putBoolean(getString(R.string.switchlimit), isChecked).apply()
            // Muestra u oculta la barra de progreso según el estado del interruptor
            seekBar?.visibility = if (isChecked) View.VISIBLE else View.GONE
            // Muestra u oculta el TextView según el estado del interruptor
            percentTextView?.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (isChecked) {
                //createChannel()
                //createNotification()
            }


        }
        switchAlertBalance.setOnCheckedChangeListener { _, isChecked ->
            Utils.applyTheme(isChecked)
            sharedPreferences.edit().putBoolean(getString(R.string.switchbalance), isChecked)
                .apply()
        }
        switchWeeklyReport.setOnCheckedChangeListener { _, isChecked ->
            Utils.applyTheme(isChecked)
            sharedPreferences.edit().putBoolean(getString(R.string.switchweek), isChecked).apply()
        }
        switchMonthlyReport.setOnCheckedChangeListener { _, isChecked ->
            Utils.applyTheme(isChecked)
            sharedPreferences.edit().putBoolean(getString(R.string.switchmonth), isChecked).apply()
        }


        // Asigna un listener de cambio de progreso a la barra de progreso
        // Dentro de tu clase o función donde estás manejando el SeekBar y el TextView
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {


                // Actualiza el valor del progreso y muestra el porcentaje en el TextView
                progressValue = progress
                percentTextView?.text = "$progress%"

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
// Recupera el progreso de SharedPreferences cuando se inicia tu actividad o fragmento
        val savedProgress = sharedPreferences.getInt("progressValue", 0)
        seekBar?.progress = savedProgress
        percentTextView?.text = "$savedProgress%"
        //Lanzamiento de notificacion

        return binding.root

    }

    /*fun createNotification() {
        // Crear un NotificationCompat.Builder
        val builder = NotificationCompat.Builder(requireContext(), CHANEL_ALERT_LIMIT)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("Alerta de gastos")
            .setContentText("Esto es una prueba")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())){

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1,builder.build())
        }
    }
    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANEL_ALERT_LIMIT,
                "channelAlert",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Puedes configurar más opciones del canal aquí, como la descripción o la importancia.
            // channel.description = "Descripción del canal"

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }*/

}
