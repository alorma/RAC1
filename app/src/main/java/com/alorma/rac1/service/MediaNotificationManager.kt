package com.alorma.rac1.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.FLAG_NO_CLEAR
import android.support.v4.content.ContextCompat
import android.support.v4.media.session.MediaSessionCompat
import com.alorma.rac1.R
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.net.Rac1Api
import com.alorma.rac1.net.ResponseNowDto
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MediaNotificationManager @Inject constructor(
        private val context: Context,
        private val rac1Api: Rac1Api
) {

    companion object {
        const val CHANNEL_GROUP_ID = "AUDIO"
        const val CHANNEL_GROUP_NAME = "Audio"

        const val CHANNEL_LIVE_ID = "LIVE"
        const val CHANNEL_LIVE_NAME = "Rádio en directe"

        const val ID_LIVE = 1
    }

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun show(sessionToken: MediaSessionCompat.Token): Notification {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createChannel(nm)


        disposable += rac1Api.now()
                .retry(3)
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    updateNotification(nm, it, sessionToken)
                }, {

                })
        return createNotification(sessionToken)
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

    private fun createNotification(sessionToken: MediaSessionCompat.Token): Notification {
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.rac1_micro)

        return NotificationCompat.Builder(context, CHANNEL_LIVE_ID).apply {
            setContentTitle(context.getString(R.string.live_radio_title))
            configBaseNotification(largeIcon, sessionToken)
        }.build().apply {
            flags = FLAG_NO_CLEAR
        }
    }

    private fun updateNotification(nm: NotificationManager, dto: ResponseNowDto, sessionToken: MediaSessionCompat.Token) {
        val data = dto.result.program

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.rac1_micro)

        val notification = NotificationCompat.Builder(context, CHANNEL_LIVE_ID).apply {
            setContentTitle(data.title)
            setSubText(data.subtitle)
            configBaseNotification(largeIcon, sessionToken)
        }.build().apply {
            flags = FLAG_NO_CLEAR
        }

        nm.notify(ID_LIVE, notification)
    }

    private fun NotificationCompat.Builder.configBaseNotification(largeIcon: Bitmap,
                                                                  sessionToken: MediaSessionCompat.Token) {
        setLargeIcon(largeIcon)
        setStyle(buildStyle(sessionToken))
        setSmallIcon(R.drawable.ic_rss)
        color = ContextCompat.getColor(context, R.color.colorPrimary)
        val stopAction = NotificationCompat.Action.Builder(R.drawable.ic_stop,
                context.getString(R.string.live_radio_stop),
                getActionPendingIntentStop()).build()
        addAction(stopAction)
    }

    private fun getActionPendingIntentStop(): PendingIntent? {
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

    fun destroy() {
        disposable.clear()
    }
}