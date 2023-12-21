package carnerero.agustin.cuentaappandroid.utils

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import carnerero.agustin.cuentaappandroid.MainActivity
import carnerero.agustin.cuentaappandroid.NotificationsFragment
import carnerero.agustin.cuentaappandroid.R

class AlarmNotifications:BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        TODO("Not yet implemented")
    }
    /*private fun createNotification() {
        val intent= Intent(this, MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag= PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this,
            0, intent,
            flag)
        // Crear un NotificationCompat.Builder
        val builder = NotificationCompat.Builder(this,
            NotificationsFragment.CHANEL_ALERT_LIMIT
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Alerta de gastos")
            .setContentText("Esto es una prueba")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(this)){

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(1,builder.build())
        }
    }*/
}