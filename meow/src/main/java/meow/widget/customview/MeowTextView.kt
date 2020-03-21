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

package meow.widget.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.etebarian.meowframework.R
import meow.controller
import meow.util.MeowDrawableHelper
import meow.util.MeowShapeDrawable
import meow.util.avoidException
import meow.util.getFont
import meow.widget.impl.MeowShapeDrawableImpl
import meow.widget.impl.TextViewImpl

/**
 * The TextView Widget.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-06
 */

@Suppress("LeakingThis", "unused")
open class MeowTextView : AppCompatTextView, TextViewImpl, MeowShapeDrawableImpl {

    override var fontPath: String? = ""
        set(value) {
            field = value
            typeface = context.getFont(fontPath)
        }
    override var isPersian = controller.isPersian
    override var beforeText: String? = ""
    override var afterText: String? = ""
    override var hasSpan = false
    override var forcePaddingFont = controller.isForceFontPadding
    override var forceGravity = true
    override var shapeDrawable: MeowShapeDrawable? = null
        set(value) {
            field = value
            draw()
        }
    var hasCustomBackground = false
    private var reverseGravity = false

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

        setAttributeFromXmlShapeDrawable(context, attrs)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowTextView, 0, 0)
        avoidException(
            tryBlock = {
                a.apply {
                    hasCustomBackground =
                        getBoolean(
                            R.styleable.MeowTextView_hasCustomBackground,
                            hasCustomBackground
                        )
                    reverseGravity =
                        getBoolean(R.styleable.MeowTextView_reverseGravity, reverseGravity)
                }
            },
            finallyBlock = { a.recycle() })
    }

    private fun initializeView() {
        fontPath = fontPath
        includeFontPadding = forcePaddingFont
        text = text.toString()
        gravity = gravity
        draw()
    }

    private fun draw() {
        if (hasCustomBackground)
            background = MeowDrawableHelper().createMeowShapeDrawable(context, shapeDrawable)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(mergeBeforeAndAfterText(text), type)
    }

    override fun setGravity(gravity: Int) {
        super.setGravity(calculateGravityIfNeed(gravity, reverseGravity))
    }

    override fun setTextColor(color: Int) {
        val newColor = controller.onColorGet(color)
        super.setTextColor(newColor)
    }

    override fun setTextColor(colors: ColorStateList) {
        val newColor = controller.onColorGet(colors.defaultColor)
        super.setTextColor(newColor)
    }

}