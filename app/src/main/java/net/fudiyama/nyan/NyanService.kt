package net.fudiyama.nyan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager

class NyanService : Service() {
    private lateinit var rfcommServer: RfcommServer

    override fun onBind(p0: Intent?): IBinder? {
        // We do not expect to use binders at this time.
        return null
    }

    override fun onCreate() {
        super.onCreate()

        rfcommServer = RfcommServer(this)
        rfcommServer.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.foreground_service_channel_id)
            val name = getString(R.string.app_name)

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            var channel = manager.getNotificationChannel(channelId)
            if (channel == null) {
                channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
                manager.createNotificationChannel(channel)
            }

            val notification =
                NotificationCompat.Builder(this, channelId).apply {
                    setContentTitle(getString(R.string.app_name))
                    setContentText(getString(R.string.how_to_stop_service))
                    setSmallIcon(R.mipmap.ic_launcher)
                }.build()

            // FIXME Manage IDs so that they are not duplicated in the application.
            startForeground(1, notification)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        rfcommServer.stop()
    }

    companion object {
        fun isEnabled(context: Context): Boolean {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPrefs.getBoolean(
                context.getString(R.string.prefs_is_service_enabled),
                false
            )
        }

        fun setEnabled(context: Context, isEnabled: Boolean) {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
            sharedPrefs.edit()
                .putBoolean(context.getString(R.string.prefs_is_service_enabled), isEnabled).apply()
        }

        fun startNyanService(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, NyanService::class.java))
            } else {
                context.startService(Intent(context, NyanService::class.java))
            }
        }

        fun stopNyanService(context: Context) {
            context.stopService(Intent(context, NyanService::class.java))
        }
    }
}