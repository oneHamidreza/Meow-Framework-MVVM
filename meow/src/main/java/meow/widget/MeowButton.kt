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

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import meow.R
import meow.controller
import meow.utils.ColorHelper
import meow.utils.avoidExceptionFinal
import meow.utils.getFont
import meow.widget.impl.TextViewImpl

/**
 * The TextField Widget.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-07
 */

@Suppress("LeakingThis")
open class MeowButton : MaterialButton, TextViewImpl {

    override var fontPath: String? = ""
    override var isPersian = controller.isPersian
    override var beforeText: String? = ""
    override var afterText: String? = ""
    override var hasSpan = false
    override var forcePaddingFont = controller.isForceFontPadding
    override var forceGravity = false

    @Suppress("MemberVisibilityCanBePrivate")
    protected var calculateRipple = true

    @Suppress("MemberVisibilityCanBePrivate")
    protected var rippleValue = 0.64f

    var isAttachToForm = false

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    override fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
        super.setAttributeFromXml(context, attrs)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowButton, 0, 0)
        avoidExceptionFinal({
            a.apply {
                calculateRipple =
                    getBoolean(R.styleable.MeowButton_calculateRipple, calculateRipple)
                rippleValue = getFloat(R.styleable.MeowButton_rippleValue, rippleValue)
                isAttachToForm = getBoolean(R.styleable.MeowButton_attachToForm, isAttachToForm)
            }
        }) { a.recycle() }
    }

    private fun initializeView() {
        typeface = context.getFont(fontPath)
        includeFontPadding = forcePaddingFont
        text = text.toString()
        rippleColor = rippleColor
        backgroundTintList = backgroundTintList
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(checkAndGetText(text), type)
    }

    override fun setRippleColor(rippleColor: ColorStateList?) {
        var newRippleColor = rippleColor
        if (controller.changeColor) {
            newRippleColor = ColorStateList.valueOf(
                controller.onColorGet(
                    context, rippleColor?.defaultColor
                        ?: 0
                )
            )
        }

        if (!calculateRipple && newRippleColor != null) {
            super.setRippleColor(rippleColor)
            return
        }

        super.setRippleColor(
            ColorStateList.valueOf(
                ColorHelper.setAlpha(
                    newRippleColor?.defaultColor
                        ?: 0, rippleValue
                )
            )
        )
    }

    override fun setBackgroundTintList(tintList: ColorStateList?) {
        if (!controller.changeColor) {
            super.setBackgroundTintList(tintList)
            return
        }

        super.setBackgroundTintList(
            ColorStateList.valueOf(
                controller.onColorGet(
                    context, tintList?.defaultColor
                        ?: 0
                )
            )
        )
    }

    override fun setGravity(gravity: Int) {
        super.setGravity(calculateGravityIfNeed(gravity))
    }

    override fun setBackgroundColor(color: Int) {
        val newColor = controller.onColorGet(context, color)
        super.setBackgroundColor(newColor)
    }

    override fun setTextColor(color: Int) {
        val newColor = controller.onColorGet(context, color)
        super.setTextColor(newColor)
    }

    override fun setTextColor(colors: ColorStateList) {
        val newColor = controller.onColorGet(context, colors.defaultColor)
        super.setTextColor(newColor)
    }
}