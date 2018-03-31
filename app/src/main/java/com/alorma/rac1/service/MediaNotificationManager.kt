package com.alorma.rac1.service

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.media.session.MediaSessionCompat
import com.alorma.rac1.R
import javax.inject.Inject

class MediaNotificationManager @Inject constructor(private val context: Context) {

    companion object {
        const val CHANNEL_GROUP_ID = "AUDIO"
        const val CHANNEL_GROUP_NAME = "Audio"

        const val CHANNEL_LIVE_ID = "LIVE"
        const val CHANNEL_LIVE_NAME = "Rádio en directe"

        const val ID_LIVE = 1
    }

    fun show(sessionToken: MediaSessionCompat.Token) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createChannel(nm)
        createNotification(nm, sessionToken)
    }

    fun hide() {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        nm.cancel(ID_LIVE)
    }

    private fun createChannel(nm: NotificationManager) {
        nm.createNotificationChannelGroup(NotificationChannelGroup(CHANNEL_GROUP_ID, CHANNEL_GROUP_NAME))


        val channel = NotificationChannel(CHANNEL_LIVE_ID, CHANNEL_LIVE_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            lightColor = ContextCompat.getColor(context, R.color.colorPrimary)

        }
        nm.createNotificationChannel(channel)
    }

    private fun createNotification(nm: NotificationManager, sessionToken: MediaSessionCompat.Token) {

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.rac1_micro)

        val notification = NotificationCompat.Builder(context, CHANNEL_LIVE_ID).apply {
            setStyle(buildStyle(sessionToken))
            setSmallIcon(R.drawable.ic_rss)
            setLargeIcon(largeIcon)
            setContentTitle("Rádio en directe")
            color = ContextCompat.getColor(context, R.color.colorPrimary)
            val stopAction = NotificationCompat.Action.Builder(R.drawable.ic_stop, "Stop", getActionPendingIntentStop()).build()
            addAction(stopAction)
        }.build()

        nm.notify(ID_LIVE, notification)
    }

    fun getActionPendingIntentStop(): PendingIntent? {
        val intent = Intent(context, LiveRadioService::class.java).apply {
            putExtra(LiveRadioService.CMD, LiveRadioService.CMD_STOP)
        }

        return PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private fun buildStyle(sessionToken: MediaSessionCompat.Token): NotificationCompat.Style {
        return android.support.v4.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(sessionToken)
                .setShowActionsInCompactView(0)
    }

}