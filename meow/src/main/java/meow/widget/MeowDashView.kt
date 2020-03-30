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
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.etebarian.meowframework.R
import meow.util.getColorCompat

/**
 * Meow Dash View class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-19
 */

@Suppress("unused")
class MeowDashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : View(context, attrs, defStyleAttrs) {

    private var orientation = Orientation.HORIZONTAL
    private var gap = 0f
    private var length = 0f
    private var thickness = 0f
    private var lineColor = 0

    private lateinit var paint: Paint

    init {
        setAttributesFromXml(attrs, R.styleable.MeowDashView) {
            orientation = Orientation.values()[it.getInt(
                R.styleable.MeowDashView_dash_orientation,
                orientation.ordinal
            )]
            gap = it.getDimension(R.styleable.MeowDashView_dash_gap, gap)
            length = it.getDimension(R.styleable.MeowDashView_dash_length, length)
            thickness = it.getDimension(R.styleable.MeowDashView_dash_thickness, thickness)
            lineColor = it.getColorCompat(R.styleable.MeowDashView_dash_color, lineColor)
        }
        initializeView()
    }

    private fun initializeView() {
        paint = Paint().apply {
            isAntiAlias = true
            color = lineColor
            style = Paint.Style.STROKE
            strokeWidth = thickness
            pathEffect = DashPathEffect(floatArrayOf(length, gap), 0f)
        }
        setLayerType(LAYER_TYPE_SOFTWARE, paint)
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == Orientation.HORIZONTAL) {
            val center = height * .5f
            canvas.drawLine(0f, center, width.toFloat(), center, paint)
        } else {
            val center = width * .5f
            canvas.drawLine(center, 0f, center, height.toFloat(), paint)
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

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }
}