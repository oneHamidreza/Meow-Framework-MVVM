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

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.widget.Toast
import meow.controller
import meow.core.ui.FragmentActivityInterface
import meow.core.ui.MeowActivity
import meow.core.ui.MeowFragment

/**
 * [Toast] Extensions for use in [FragmentActivityInterface] instances like [MeowActivity], [MeowFragment] .
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-16
 */

object ToastUtil {
    var toast: Toast? = null
}

fun FragmentActivityInterface<*>?.toastL(
    res: Int,
    gravity: Int = 0,
    xOffset: Int = 0,
    yOffset: Int = 0
) {
    if (this == null)
        return
    context().toast(resources().getString(res), Toast.LENGTH_LONG, gravity, xOffset, yOffset)
}

fun FragmentActivityInterface<*>?.toastS(
    res: Int,
    gravity: Int = 0,
    xOffset: Int = 0,
    yOffset: Int = 0
) {
    if (this == null)
        return
    context().toast(resources().getString(res), Toast.LENGTH_SHORT, gravity, xOffset, yOffset)
}

fun FragmentActivityInterface<*>?.toastL(
    message: String?,
    gravity: Int = 0,
    xOffset: Int = 0,
    yOffset: Int = 0
) {
    if (this == null)
        return
    context().toast(message, Toast.LENGTH_LONG, gravity, xOffset, yOffset)
}

fun FragmentActivityInterface<*>?.toastS(
    message: String?,
    gravity: Int = 0,
    xOffset: Int = 0,
    yOffset: Int = 0
) {
    if (this == null)
        return
    context().toast(message, Toast.LENGTH_SHORT, gravity, xOffset, yOffset)
}

private fun Context?.toast(
    message: String?,
    duration: Int,
    gravity: Int,
    xOffset: Int,
    yOffset: Int
) {
    if (this == null || message == null)
        return

    avoidException {
        ToastUtil.toast?.cancel()

        val font = getFontCompat(controller.toastTypefaceResId)
        val span = SpannableString(message)
        span.setSpan(
            CustomTypefaceSpan(font),
            0,
            span.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ToastUtil.toast = Toast.makeText(this, span, duration).apply {
            if (gravity != 0)
                setGravity(gravity, xOffset, yOffset)

            show()
        }
    }
}

private fun MeowFragment<*>?.toast(
    message: String?,
    duration: Int,
    gravity: Int,
    xOffset: Int,
    yOffset: Int
) =
    this?.context().toast(message, duration, gravity, xOffset, yOffset)

class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

    override fun updateMeasureState(p: TextPaint) {
        p.typeface = typeface
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.typeface = typeface
        tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
    }
}