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
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.etebarian.meowframework.R


/**
 * Meow Circle Image View class.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-04-18
 */

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
open class MeowCircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttrs) {

    var hasStroke = false
        set(value) {
            field = value
            draw()
        }
    var strokeColor = 0
        set(value) {
            field = value
            draw()
        }
    var strokeWidth = 0f
        set(value) {
            field = value
            if (allowDraw)//todo @Ali without this
                draw()
        }

    private var allowDraw = false

    init {
        setAttributesFromXml(attrs, R.styleable.MeowCircleImageView) {
            strokeColor =
                it.getColor(R.styleable.MeowCircleImageView_meow_strokeColor, strokeColor)
            strokeWidth =
                it.getDimension(R.styleable.MeowCircleImageView_meow_strokeWidth, strokeWidth)
            hasStroke = it.getBoolean(R.styleable.MeowCircleImageView_meow_hasStroke, hasStroke)
        }

        hasStroke = hasStroke
        strokeColor = strokeColor
        strokeWidth = strokeWidth

        initializeView()
    }

    private fun initializeView() {
        allowDraw = true
        draw()
    }

    private fun draw() {
        if (!allowDraw)
            return

        if (hasStroke) {
            val sw = strokeWidth.toInt()
            setPadding(sw, sw, sw, sw)
            val drawable = GradientDrawable()
            drawable.apply {
                shape = GradientDrawable.OVAL
                setColor(strokeColor)
            }
            background = drawable
        } else {
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        }
    }

}