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

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.TextViewCompat
import com.google.android.material.snackbar.Snackbar
import meow.core.ui.MVVM
import meow.core.ui.MeowActivity
import meow.core.ui.MeowFragment

/**
 * [Toast] Extensions for use in [MVVM] instances like [MeowActivity], [MeowFragment] .
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-17
 */

fun MVVM<*, *>?.snackL(
    message: String,
    actionText: String? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (this == null)
        return null
    return snack(
        message,
        Snackbar.LENGTH_LONG,
        actionText = actionText,
        messageTextAppearanceId = messageTextAppearanceId,
        actionTextAppearanceId = actionTextAppearanceId,
        onActionClicked = onActionClicked
    )
}

fun MVVM<*, *>?.snackS(
    message: String,
    actionText: String? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (this == null)
        return null
    return snack(
        message,
        Snackbar.LENGTH_SHORT,
        actionText = actionText,
        messageTextAppearanceId = messageTextAppearanceId,
        actionTextAppearanceId = actionTextAppearanceId,
        onActionClicked = onActionClicked
    )
}

fun MVVM<*, *>?.snackL(
    resMessage: Int,
    resActionText: Int? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (this == null)
        return null
    return snack(
        resources().getString(resMessage),
        Snackbar.LENGTH_LONG,
        actionText = if (resActionText != null) resources().getString(resActionText) else null,
        messageTextAppearanceId = messageTextAppearanceId,
        actionTextAppearanceId = actionTextAppearanceId,
        onActionClicked = onActionClicked
    )
}

fun MVVM<*, *>?.snackS(
    resMessage: Int,
    resActionText: Int? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (this == null)
        return null
    return snack(
        resources().getString(resMessage),
        Snackbar.LENGTH_SHORT,
        actionText = if (resActionText != null) resources().getString(resActionText) else null,
        messageTextAppearanceId = messageTextAppearanceId,
        actionTextAppearanceId = actionTextAppearanceId,
        onActionClicked = onActionClicked
    )
}

fun MVVM<*, *>?.snackI(
    resMessage: Int,
    resActionText: Int? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (this == null)
        return null
    return snack(
        resources().getString(resMessage),
        Snackbar.LENGTH_INDEFINITE,
        actionText = if (resActionText != null) resources().getString(resActionText) else null,
        messageTextAppearanceId = messageTextAppearanceId,
        actionTextAppearanceId = actionTextAppearanceId,
        onActionClicked = onActionClicked
    )
}

fun MVVM<*, *>?.snackI(
    message: String,
    actionText: String? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (this == null)
        return null
    return snack(
        message,
        Snackbar.LENGTH_INDEFINITE,
        actionText = actionText,
        messageTextAppearanceId = messageTextAppearanceId,
        actionTextAppearanceId = actionTextAppearanceId,
        onActionClicked = onActionClicked
    )
}

fun MVVM<*, *>?.snack(
    message: String?,
    duration: Int,
    contentView: View = this?.contentView()!!,
    actionText: String? = null,
    messageTextAppearanceId: Int = 0,
    actionTextAppearanceId: Int = 0,
    onActionClicked: () -> Unit = { }
): Snackbar? {
    if (message == null)
        return null

    return avoidException {
        Snackbar.make(contentView, message, duration).apply {
            if (actionText != null)
                setAction(actionText) {
                    onActionClicked()
                }

            val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            val bt = view.findViewById(com.google.android.material.R.id.snackbar_action) as Button

            if (messageTextAppearanceId != 0)
                TextViewCompat.setTextAppearance(tv, messageTextAppearanceId)
            if (actionTextAppearanceId != 0)
                TextViewCompat.setTextAppearance(bt, actionTextAppearanceId)

            show()
        }
    }
}