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

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.etebarian.meowframework.R
import meow.controller
import meow.util.*
import meow.widget.impl.MeowShapeDrawableImpl
import kotlin.math.ceil

/**
 * The TextField Widget.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-13
 */
@Suppress("unused", "LeakingThis", "MemberVisibilityCanBePrivate")
open class MeowImageView : AppCompatImageView, MeowShapeDrawableImpl {

    var isBitmap = false
        set(value) {
            field = value
            draw()
        }
    var useColor = true
        set(value) {
            field = value
            draw()
        }
    var resource = 0
        set(value) {
            field = value
            draw()
        }
    var color = 0
        set(value) {
            field = value
            draw()
        }
        get() {
            return controller.onColorGet(field)
        }
    var size = dip(24)
        set(value) {
            field = value
            requestLayout()
        }
    override var shapeDrawable: MeowShapeDrawable? = null
    private var isAction = false
    private var actionBackgroundAlpha = false
    private var changeSize = true
    private var fitImage = false
    private var colorAnimator: ValueAnimator? = null
    private var allowDraw = false
    private var first = ""
    private var second = ""
    private var isRatio = false

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    open fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
        super.setAttributeFromXmlShapeDrawable(context, attrs)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeowImageView, 0, 0)
        avoidExceptionFinal({
            a?.apply {
                isBitmap = getBoolean(R.styleable.MeowImageView_meow_isBitmap, isBitmap)
                useColor = getBoolean(R.styleable.MeowImageView_meow_useColor, useColor)
                resource = getResourceId(R.styleable.MeowImageView_meow_src, resource)
                color = getColor(R.styleable.MeowImageView_meow_bitmapColor, color)
                size = getDimensionPixelSize(R.styleable.MeowImageView_meow_imageSize, size)
                isAction = getBoolean(R.styleable.MeowImageView_meow_isAction, isAction)
                actionBackgroundAlpha = getBoolean(R.styleable.MeowImageView_meow_actionBackgroundAlpha, actionBackgroundAlpha)
                changeSize = getBoolean(R.styleable.MeowImageView_meow_changeSize, changeSize)
                fitImage = getBoolean(R.styleable.MeowImageView_meow_fitImage, fitImage)
                val ratio = getString(R.styleable.MeowImageView_meow_aspectRatio)
                isRatio = getBoolean(R.styleable.MeowImageView_meow_aspectRatioEnabled, false)
                if (!ratio.isNullOrEmpty()) {
                    second = ratio.substring(ratio.lastIndexOf(":") + 1)
                    first = ratio.replace(":$second", "")
                }
            }
        }) { a?.recycle() }
    }

    private fun initializeView() {
        allowDraw = true
        draw()
    }

    private fun draw() {
        if (!allowDraw)
            return

        if (isAction) {
            if (actionBackgroundAlpha) {
                shapeDrawable?.mainColorAlpha = 0.12f
                shapeDrawable?.rippleColorAlpha = 0.64f
            }
            if (color != 0)
                shapeDrawable?.rippleColor = color
            background = MeowDrawableHelper().createMeowShapeDrawable(context, shapeDrawable)
        }

        if (resource == 0)
            return

        if (isBitmap) {
            avoidException {
                val drawable = if (color == 0) context.getDrawableCompat(resource) else MeowDrawableHelper().changeColorDrawableRes(context, resource, color)
                setImageDrawable(drawable)
            }
            return
        }

        if (useColor && color == 0)
            return

        val c = if (useColor) color else -2
        avoidException {
            setImageDrawable(MeowDrawableHelper().changeColorDrawableVector(context, resource, c))
        }
    }

    fun changeColorByAnim(newColor: Int, d: Long = 250L) {
        if (color == 0) {
            color = newColor
            return
        }
        val lastColor = color

        colorAnimator?.cancel()

        colorAnimator = ValueAnimator.ofFloat(0f, 1f)
        colorAnimator?.apply {
            duration = d
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { animation ->
                val f = animation.animatedFraction
                color = ColorHelper.mixTwoColors(newColor, lastColor, f)
            }
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isRatio) {
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightSize = (second.toFloat() / first.toFloat() * widthSize).toInt()
            val newHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, newHeightSpec)
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (fitImage) {
            val d = drawable
            if (d != null) {
                val width = MeasureSpec.getSize(widthMeasureSpec)
                val height = ceil((width.toFloat() * d.intrinsicHeight.toFloat() / d.intrinsicWidth).toDouble()).toInt()
                setMeasuredDimension(width, height)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
            return
        }

        if (isBitmap || !changeSize) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val newSize = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(newSize, newSize)
    }

}