/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.etebarian.meowframework.R
import meow.core.ui.MVVM

/**
 * Notification Utils.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-19
 */

class MeowNotificationUtils {

    companion object {

        private const val CHANNEL_ID_DEFAULT = "default"
    }

    fun createBuilder(
        context: Context,
        title: String?,
        contentText: String?,
        smallIconRes: Int,
        bigContent: String? = contentText,
        pendingIntent: PendingIntent? = null,
        autoCancel: Boolean = true,
        channelId: String = CHANNEL_ID_DEFAULT,
        color: Int = context.getColorCompat(R.color.notification),
        number: Int = 0
    ): NotificationCompat.Builder {

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIconRes)
            .setContentTitle(title)
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setAutoCancel(autoCancel)
            .setColor(color)
            .setLights(color, 300, 1000)

        if (number > 0)
            builder.setNumber(number)

        if (bigContent.isNotNullOrEmpty()) {
            builder.setStyle(NotificationCompat.BigTextStyle().also {
                it.setBigContentTitle(title)
                it.bigText(bigContent)
            })
        }

        return builder
    }

    fun createChannel(context: Context, channelName: String = CHANNEL_ID_DEFAULT) {
        if (Build.VERSION.SDK_INT >= 26) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(channelName, context.getString(R.string.app_name), importance)
            channel.description = context.getString(R.string.app_name)
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.lightColor = context.getColorCompat(R.color.notification)
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)
        }
    }

    fun notify(context: Context, id: Int, builder: NotificationCompat.Builder) {
        avoidException {
            createChannel(context)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, builder.build())
        }
    }

}

fun Context?.cancelNotification(id: Int) {
    if (this == null)
        return
    avoidException {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(id)
    }
}

fun MVVM<*, *>?.cancelNotification(id: Int) {
    if (this == null)
        return
    avoidException {
        (context().getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancel(
            id
        )
    }
}

fun Context?.cancelAllNotifications() {
    if (this == null)
        return
    avoidException {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
    }
}

fun MVVM<*, *>?.cancelAllNotifications() {
    if (this == null)
        return
    avoidException {
        (context().getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
    }
}