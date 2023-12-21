package carnerero.agustin.cuentaappandroid.utils


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import carnerero.agustin.cuentaappandroid.MainActivity
import carnerero.agustin.cuentaappandroid.NotificationsFragment
import carnerero.agustin.cuentaappandroid.R

class AlarmNotifications:BroadcastReceiver() {
    companion object{
        const val ALARM_LIMIT_NOTIFICATION=1
    }
    override fun onReceive(context: Context, p1: Intent?) {
        createNotification(context)
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
            NotificationsFragment.CHANEL_ALERT_LIMIT
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
}