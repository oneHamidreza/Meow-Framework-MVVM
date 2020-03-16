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

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

/**
 * Meow Typeface Span class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-14
 */

class MeowTypefaceSpan(family: String, private val newType: Typeface?) : TypefaceSpan(family) {

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType!!)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType!!)
    }
}
