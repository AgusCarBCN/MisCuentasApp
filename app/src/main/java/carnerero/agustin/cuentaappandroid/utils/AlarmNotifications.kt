package carnerero.agustin.cuentaappandroid.utils


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.contentValuesOf
import carnerero.agustin.cuentaappandroid.MainActivity
import carnerero.agustin.cuentaappandroid.NotificationsFragment
import carnerero.agustin.cuentaappandroid.R

class AlarmNotifications:BroadcastReceiver() {
    companion object{
        const val ALARM_LIMIT_NOTIFICATION=1
        const val ALARM_BALANCE=2
        const val REPORT_WEEKLY=3
        const val REPORT_MONTLY=4
    }
    override fun onReceive(context: Context, intent: Intent?) {
        // Verifica si el intent no es nulo y contiene datos adicionales (si es necesario)
        if (intent != null) {
            val notificationType = intent.getIntExtra("notificationType", -1)

            // Asegúrate de tener un valor válido para notificationType
            if (notificationType != -1) {
                createNotification2(context, notificationType)
            }
        }

    }
private fun createNotification(context: Context) {
        val intent= Intent(context, MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag= PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context,
            0, intent,
            flag)
        // Crear un NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context,
            NotificationsFragment.CHANEL_NOTIFICATIONS
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Alerta de gastos")
            .setContentText("Esto es una prueba")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val manager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(ALARM_LIMIT_NOTIFICATION,notification)
    }
    private fun createNotification2(context: Context, notificationType: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, NotificationsFragment.CHANEL_NOTIFICATIONS)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        when (notificationType) {
            ALARM_LIMIT_NOTIFICATION -> {
                notificationBuilder.setContentTitle(context.getString(R.string.limitexpense))
                    .setContentText("Esto es una prueba para la notificación de límite de gastos")
            }
            ALARM_BALANCE -> {
                notificationBuilder.setContentTitle(context.getString(R.string.balance))
                    .setContentText("Esto es una prueba para la notificación de alerta de saldo")
            }
            REPORT_WEEKLY -> {
                notificationBuilder.setContentTitle(context.getString(R.string.weekreport))
                    .setContentText("Esto es una prueba para la notificación de informe semanal")
            }
            REPORT_MONTLY -> {
                notificationBuilder.setContentTitle(context.getString(R.string.monthreport))
                    .setContentText("Esto es una prueba para la notificación de informe mensual")
            }
            else -> {
                // Puedes agregar un caso predeterminado o manejar otros tipos de notificaciones según sea necesario
            }
        }

        val notification = notificationBuilder.build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationType, notification)
    }

}