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
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.etebarian.meowframework.R
import meow.util.dp
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

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    private var orientation = Orientation.HORIZONTAL
    var gap = 2f.dp()
        set(value) {
            field = value
            paint.pathEffect = DashPathEffect(floatArrayOf(length, gap), 0f)
            if (isAttachedToWindow)
                invalidate()
        }
    var length = 2f.dp()
        set(value) {
            field = value
            paint.pathEffect = DashPathEffect(floatArrayOf(length, gap), 0f)
            if (isAttachedToWindow)
                invalidate()
        }
    var thickness = 1f.dp()
        set(value) {
            field = value
            paint.strokeWidth = value
            if (isAttachedToWindow) {
                requestLayout()
                invalidate()
            }
        }
    var dashColor = Color.BLACK
        set(value) {
            field = value
            paint.color = value
            if (isAttachedToWindow)
                invalidate()
        }

    private var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, paint)

        setAttributesFromXml(attrs, R.styleable.MeowDashView) {
            orientation = Orientation.values()[it.getInt(
                R.styleable.MeowDashView_dash_orientation,
                orientation.ordinal
            )]
            gap = it.getDimension(R.styleable.MeowDashView_dash_gap, gap)
            length = it.getDimension(R.styleable.MeowDashView_dash_length, length)
            thickness = it.getDimension(R.styleable.MeowDashView_dash_thickness, thickness)
            dashColor = it.getColorCompat(R.styleable.MeowDashView_dash_color, dashColor)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == Orientation.HORIZONTAL) {
            val center = measuredHeight * .5f
            canvas.drawLine(0f, center, measuredWidth.toFloat(), center, paint)
        } else {
            val center = measuredWidth * .5f
            canvas.drawLine(center, 0f, center, measuredHeight.toFloat(), paint)
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