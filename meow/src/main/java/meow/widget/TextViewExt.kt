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

package meow.widget

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter
import meow.controller
import meow.util.toPersianNumber

/**
 * [android.widget.TextView] Extensions.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-15
 */

object TextViewBindingAdapter {

    @BindingAdapter("forceFontPadding")
    fun setFontPadding(view: TextView, forceFontPadding: Boolean) {
        view.includeFontPadding = controller.isForceFontPadding
    }

    @BindingAdapter("android:gravity", "reverseGravity", "forceGravity", requireAll = false)
    @JvmStatic
    fun setGravity(view: TextView, gravity: Int, reverseGravity: Boolean, forceGravity: Boolean) {
        view.gravity = calculateGravityIfNeed(gravity, reverseGravity, forceGravity)
    }


    @SuppressLint("SetTextI18n")
    @BindingAdapter("prefixText")
    @JvmStatic
    fun setPrefix(view: TextView, prefixText: String) {
        view.text = "$prefixText${view.text}"
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("suffixText")
    @JvmStatic
    fun setSuffix(view: TextView, suffixText: String) {
        view.text = "${view.text}$suffixText"
    }

    @BindingAdapter("isPersianNumber")
    @JvmStatic
    fun setPersianNumber(view: TextView, isPersianNumber: Boolean) {
        view.text = if (isPersianNumber) view.text.toString().toPersianNumber() else view.text
    }

    @BindingAdapter("android:textColor")
    @JvmStatic
    fun setTextColor(view: TextView, textColor: ColorStateList) {
        view.setTextColor(controller.onColorStateListGet(textColor))
    }

    @BindingAdapter("android:textColor")
    @JvmStatic
    fun setTextColor(view: TextView, textColor: Int) {
        view.setTextColor(controller.onColorGet(textColor))
    }
}

@SuppressLint("RtlHardcoded")
fun calculateGravityIfNeed(gravity: Int, isReverse: Boolean = false, forceGravity: Boolean): Int {
    var newGravity = gravity
    if (gravity != Gravity.CENTER && gravity != Gravity.CENTER_HORIZONTAL && forceGravity) {
        val localGravity = if (controller.isRtl) Gravity.RIGHT else Gravity.LEFT
        val inverseLocalGravity = if (controller.isRtl) Gravity.LEFT else Gravity.RIGHT

        val local = if (!isReverse) localGravity else inverseLocalGravity
        val inverse = if (!isReverse) inverseLocalGravity else localGravity

        if (gravity / Gravity.START != 0) {
            val defG = gravity - Gravity.START
            newGravity = local or defG
        } else if (gravity / Gravity.END != 0) {
            val defG = gravity - Gravity.END
            newGravity = inverse or defG
        }
    }
    return newGravity
}

