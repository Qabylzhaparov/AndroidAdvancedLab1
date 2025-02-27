package com.example.lab1.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.lab1.R

class ForegroundService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val channelId = "music_service_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        mediaPlayer = MediaPlayer()
        try {
            val assetFileDescriptor = assets.openFd("music.mp3")
            mediaPlayer?.apply {
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                prepare()
                isLooping = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> startMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopSelf()
        }
        return START_STICKY
    }

    private fun startMusic() {
        mediaPlayer?.start()
        startForeground(1, createNotification(true))
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        startForeground(1, createNotification(false))
    }

    private fun createNotification(isPlaying: Boolean): Notification {
        val playIntent = PendingIntent.getService(this, 0, Intent(this, ForegroundService::class.java).apply {
            action = if (isPlaying) "PAUSE" else "PLAY"
        }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = PendingIntent.getService(this, 0, Intent(this, ForegroundService::class.java).apply {
            action = "STOP"
        }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Player")
            .setContentText(if (isPlaying) "Playing" else "Paused")
            .setSmallIcon(R.drawable.ic_music)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1))
            .addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                "Play/Pause",
                playIntent
            )
            .addAction(R.drawable.ic_stop, "Stop", stopIntent)
            .setOngoing(isPlaying)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Music Service", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
