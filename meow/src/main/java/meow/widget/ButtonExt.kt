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
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import meow.controller
import meow.util.MeowColorUtils
import meow.util.toPersianNumber

/**
 * [android.widget.Button] Extensions.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-19
 */

object ButtonBindingAdapter {
    @BindingAdapter("forceFontPadding")
    fun setFontPadding(view: Button, forceFontPadding: Boolean) {
        view.includeFontPadding = controller.isForceFontPadding
    }

    @BindingAdapter("android:gravity", "reverseGravity", "forceGravity", requireAll = false)
    @JvmStatic
    fun setGravity(view: Button, gravity: Int, reverseGravity: Boolean, forceGravity: Boolean) {
        view.gravity = calculateGravityIfNeed(gravity, reverseGravity, forceGravity)
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("prefixText")
    @JvmStatic
    fun setPrefix(view: Button, prefixText: String) {
        view.text = "$prefixText${view.text}"
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("suffixText")
    @JvmStatic
    fun setSuffix(view: Button, suffixText: String) {
        view.text = "${view.text}$suffixText"
    }

    @BindingAdapter("isPersianNumber")
    @JvmStatic
    fun setPersianNumber(view: Button, isPersianNumber: Boolean) {
        view.text = if (isPersianNumber) view.text.toString().toPersianNumber() else view.text
    }

    @BindingAdapter("backgroundTint")
    @JvmStatic
    fun setBackgroundTint(view: Button, color: ColorStateList) {
        ViewCompat.setBackgroundTintList(view, controller.onColorStateListGet(color))
    }

    @BindingAdapter("android:textColor")
    @JvmStatic
    fun setTextColor(view: Button, textColor: ColorStateList) {
        view.setTextColor(controller.onColorStateListGet(textColor))
    }

    @BindingAdapter("android:textColor")
    @JvmStatic
    fun setTextColor(view: Button, textColor: Int) {
        view.setTextColor(controller.onColorGet(textColor))
    }

    @BindingAdapter("app:rippleColor", "calculateRipple")
    @JvmStatic
    fun setRippleColor(
        view: MaterialButton,
        rippleColor: ColorStateList?,
        calculateRipple: Boolean
    ) {
        val rippleValue = 0.64f
        var newRippleColor = rippleColor
        if (controller.changeColor) {
            newRippleColor = ColorStateList.valueOf(
                controller.onColorGet(rippleColor?.defaultColor ?: 0)
            )
        }

        if (!calculateRipple && newRippleColor != null) {
            view.rippleColor = rippleColor
            return
        }

        view.rippleColor = ColorStateList.valueOf(
            MeowColorUtils.setAlpha(
                newRippleColor?.defaultColor ?: 0,
                rippleValue
            )
        )
    }

}