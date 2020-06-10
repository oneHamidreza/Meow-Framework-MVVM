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
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils
import com.etebarian.meowframework.R
import meow.ktx.dp
import meow.ktx.getMaterialColor

/**
 * Meow Divider View class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-05-02
 */

@Suppress("unused")
class MeowDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : View(context, attrs, defStyleAttrs) {

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }

    enum class Type(var res: Int) {
        BACKGROUND(R.attr.colorOnBackground),
        SURFACE(R.attr.colorOnSurface),
        PRIMARY(R.attr.colorOnPrimary),
        SECONDARY(R.attr.colorOnSurface)
    }

    var type = Type.BACKGROUND
        set(value) {
            field = value
            val color = getMaterialColor(value.res)
            background = ColorDrawable(ColorUtils.setAlphaComponent(color, (0.12 * 255).toInt()))
        }

    private var orientation = Orientation.VERTICAL

    var thickness = 1f.dp()

    init {
        setAttributesFromXml(attrs, R.styleable.MeowDivider) {
            orientation = Orientation.values()[it.getInt(
                R.styleable.MeowDivider_meow_orientation,
                orientation.ordinal
            )]

            type = Type.values()[it.getInt(
                R.styleable.MeowDivider_meow_background_type,
                type.ordinal
            )]
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == Orientation.HORIZONTAL)
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(thickness.toInt(), MeasureSpec.EXACTLY)
            )
        else
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(thickness.toInt(), MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
    }
}