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


package meow.widget.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import com.etebarian.meowframework.R
import meow.controller
import meow.utils.avoidExceptionFinal
import meow.utils.isEmptyTrim
import meow.utils.toPersianNumber

/**
 * The TextView Implementation.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-06
 */

interface TextViewImpl {

    var fontPath: String?
    var isPersian: Boolean
    var beforeText: String?
    var afterText: String?
    var hasSpan: Boolean
    var forcePaddingFont: Boolean
    var forceGravity: Boolean

    fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
        var a: TypedArray? = null
        avoidExceptionFinal({
            val fontFamilyAttr = intArrayOf(R.attr.font)
            a = context.obtainStyledAttributes(
                attrs.getAttributeResourceValue(
                    "http://schemas.android.com/apk/res/android",
                    "textAppearance",
                    0
                ), fontFamilyAttr
            )
            fontPath = a?.getString(0)
        }) { a?.recycle() }

        a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowTextView, 0, 0)
        avoidExceptionFinal({
            a?.apply {
                val attrFontFamily = getString(R.styleable.MeowTextView_fontPath)
                if (!attrFontFamily.isEmptyTrim())
                    fontPath = attrFontFamily

                beforeText = getString(R.styleable.MeowTextView_beforeText)
                afterText = getString(R.styleable.MeowTextView_afterText)

                isPersian = getBoolean(R.styleable.MeowTextView_isPersian, isPersian)
                hasSpan = getBoolean(R.styleable.MeowTextView_hasSpan, hasSpan)
                forcePaddingFont =
                    getBoolean(R.styleable.MeowTextView_forcePaddingFont, forcePaddingFont)
                forceGravity = getBoolean(R.styleable.MeowTextView_forceGravity, forceGravity)
            }
        }) { a?.recycle() }
    }

    fun getTextByAttrs(t: String): String {
        return t.apply {
            if (isPersian)
                return t.toPersianNumber()
        }
    }

    fun checkAndGetText(text: CharSequence?): CharSequence? {
        if (hasSpan)
            return text
        if (text == null)
            return ""
        return getTextByAttrs("$text")
    }

    fun mergeBeforeAndAfterText(text: CharSequence?): CharSequence? {
        if (beforeText.isNullOrEmpty() || afterText.isNullOrEmpty())
            return text
        return checkAndGetText("$beforeText$text$afterText")
    }

    @SuppressLint("RtlHardcoded")
    fun calculateGravityIfNeed(gravity: Int, isReverse: Boolean = false): Int {
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
}