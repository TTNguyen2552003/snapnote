package app.kotlin.snapnote.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.kotlin.snapnote.CHANNEL_ID
import app.kotlin.snapnote.NOTIFICATION_CHANNEL_DESCRIPTION
import app.kotlin.snapnote.NOTIFICATION_CHANNEL_NAME
import app.kotlin.snapnote.R


class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(
    appContext = context,
    params = params
) {
    override suspend fun doWork(): Result {
        val title: String = inputData.getString("note_title") ?: ""
        val body: String = inputData.getString("note_body") ?: ""
        val id: Int = inputData.getInt("note_id", 1)

        showNotification(
            id = id,
            title = title,
            body = body,
            context = applicationContext
        )

        return Result.success()
    }
}

fun showNotification(id: Int, title: String, body: String, context: Context) {
    val name: String = NOTIFICATION_CHANNEL_NAME
    val description: String = NOTIFICATION_CHANNEL_DESCRIPTION
    val importance: Int = NotificationManager.IMPORTANCE_HIGH
    val largeIcon: Bitmap? =
        BitmapFactory.decodeResource(context.resources, R.drawable.notification_icon)
    val soundUri: Uri =
        Uri.parse("android.resource://${context.packageName}/raw/notification_sound")
    val audioAttributes: AudioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()
    val channel = NotificationChannel(CHANNEL_ID, name, importance)
    channel.description = description
    channel.setSound(soundUri, audioAttributes)

    val notificationManager: NotificationManager? =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)

    val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setLargeIcon(largeIcon)
        .setContentTitle(title)
        .setContentText(body)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(size = 1))
        .setColor(Color(color = 0xffffffff).toArgb())

    NotificationManagerCompat.from(context).notify(id, builder.build())
}