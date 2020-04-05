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
import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter
import meow.controller
import meow.util.toEnglishNumber
import meow.util.toPersianNumber

/**
 * [android.widget.TextView] Extensions.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-15
 */

object TextViewBindingAdapter {

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

    @BindingAdapter("isEnglishNumber")
    @JvmStatic
    fun setEnglishNumber(view: TextView, isEnglishNumber: Boolean) {
        view.text = if (isEnglishNumber) view.text.toString().toEnglishNumber() else view.text
    }
}

@SuppressLint("RtlHardcoded")
fun calculateGravityIfNeed(gravity: Int, isReverse: Boolean = false): Int {
    var newGravity = gravity
    if (gravity != Gravity.CENTER && gravity != Gravity.CENTER_HORIZONTAL) {
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

