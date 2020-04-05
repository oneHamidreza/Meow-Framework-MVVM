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
import androidx.core.app.NotificationCompat
import com.etebarian.meowframework.R
import meow.core.ui.MeowFragment

/**
 * Notification Utils.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-19
 */

//todo improve by documents and support all features @Modares
object MeowNotificationUtils {

    private const val CHANNEL_ID_DEFAULT = "default"

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
        sdkNeed(26) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(
                    channelName,
                    context.getString(R.string.app_name),
                    importance
                ).apply {
                    description = context.getString(R.string.app_name)
                    enableVibration(true)
                    enableLights(true)
                    lightColor = context.getColorCompat(R.color.notification)
                    enableVibration(true)
                }
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

fun MeowFragment<*, *>?.cancelNotification(id: Int) {
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

fun MeowFragment<*, *>?.cancelAllNotifications() {
    if (this == null)
        return
    avoidException {
        (context().getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
    }
}