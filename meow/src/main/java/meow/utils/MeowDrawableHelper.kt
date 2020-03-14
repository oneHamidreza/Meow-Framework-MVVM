@file:Suppress("unused")

package meow.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import meow.R
import meow.controller


/**
 * Created by 1HE on 9/16/2018.
 */

open class MeowDrawableHelper {

    fun changeColorDrawableVector(c: Context?, resDrawable: Int, color: Int): Drawable? {
        if (c == null)
            return null

        val d = VectorDrawableCompat.create(c.resources, resDrawable, null) ?: return null
        d.mutate()
        if (color != -2)
            d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        if (Build.VERSION.SDK_INT > 23 && controller.isRtl && d.isAutoMirrored) {
            return flipDrawable(d)
        }
        return d
    }

    fun changeColorDrawableRes(c: Context?, resDrawable: Int, color: Int): Drawable? {
        if (c == null)
            return null

        val d = ContextCompat.getDrawable(c, resDrawable) ?: return null
        d.mutate()
        if (color != -2)
            d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return d
    }

    fun changeColorDrawable(c: Context?, d: Drawable, color: Int): Drawable? {
        if (c == null)
            return null

        d.mutate()
        if (color != -2)
            d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return d
    }

    private fun flipDrawable(d: Drawable): Drawable {
        val arD = arrayOf(d)
        return object : LayerDrawable(arD) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.scale(-1f, 1f, (d.bounds.width() / 2).toFloat(), (d.bounds.width() / 2).toFloat())
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun createMeowShapeDrawable(context: Context, d: MeowShapeDrawable?): Drawable? {
        if (d == null)
            return null

        if (d.rippleColorAlpha == 0f)
            d.rippleColorAlpha = context.getFloat(R.dimen.ripple_default)

        if (d.mainColorAlpha != 1f)
            d.mainColor = ColorHelper.setAlpha(d.mainColor, d.mainColorAlpha)
        val pressedColor = ColorHelper.getLighterOrDarkerColor(d.mainColor, 0.8f)
        val allRadius = floatArrayOf(d.topLeftRadius, d.topLeftRadius, d.topRightRadius, d.topRightRadius, d.bottomRightRadius, d.bottomRightRadius, d.bottomLeftRadius, d.bottomLeftRadius)

        if (d.isGradient) {
            val orientation = when (d.gradientOrientation) {
                0 -> GradientDrawable.Orientation.TOP_BOTTOM
                6 -> GradientDrawable.Orientation.LEFT_RIGHT
                else -> GradientDrawable.Orientation.TOP_BOTTOM
            }
            val normalDrawable = GradientDrawable(orientation, intArrayOf(d.gradientFirstColor, d.gradientSecondColor))
            normalDrawable.shape = d.shape
            normalDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            normalDrawable.gradientRadius = d.gradientRadius
            if (d.radius == 0f)
                normalDrawable.cornerRadii = allRadius
            else
                normalDrawable.cornerRadius = d.radius
            return normalDrawable
        }

        val normalDrawable = GradientDrawable()
        normalDrawable.mutate()
        normalDrawable.shape = d.shape
        if (d.radius == 0f)
            normalDrawable.cornerRadii = allRadius
        else
            normalDrawable.cornerRadius = d.radius

        val pressedDrawable = GradientDrawable()
        pressedDrawable.mutate()
        pressedDrawable.shape = d.shape
        if (d.radius == 0f)
            pressedDrawable.cornerRadii = allRadius
        else
            pressedDrawable.cornerRadius = d.radius

        if (d.hasStroke) {
            normalDrawable.setColor(d.backgroundColor)
            val strokeWidth: Int = if (d.strokeWidth > 0) d.strokeWidth.toInt() else context.resources.getDimension(R.dimen.button_stroke).toInt()
            normalDrawable.setStroke(strokeWidth, d.mainColor)
        }

        if (!d.hasStroke) {
            normalDrawable.setColor(d.mainColor)
            pressedDrawable.setColor(pressedColor)
        }

        d.rippleColor = ColorHelper.setAlpha(d.rippleColor, d.rippleColorAlpha)

        return if (Build.VERSION.SDK_INT < 21) {
            val drawable = StateListDrawable()
            if (d.hasRipple)
                drawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
            drawable.addState(intArrayOf(), normalDrawable)
            drawable
        } else {
            if (!d.hasRipple)
                return normalDrawable

            val maskDrawable = GradientDrawable()
            maskDrawable.shape = d.shape
            if (d.radius == 0f)
                maskDrawable.cornerRadii = allRadius
            else
                maskDrawable.cornerRadius = d.radius
            maskDrawable.setColor(Color.parseColor("#ffffffff"))
            if (d.transparent) {
                RippleDrawable(ColorStateList.valueOf(d.rippleColor), null, maskDrawable)
            } else {
                RippleDrawable(ColorStateList.valueOf(d.rippleColor), normalDrawable, maskDrawable)
            }
        }
    }
}

class MeowShapeDrawable {

    var shape = GradientDrawable.RECTANGLE
    var mainColor = 0
    var mainColorAlpha = 1f
    var backgroundColor = 0
    var rippleColor = 0
    var rippleColorAlpha = 0f
    var strokeWidth = 0f
    var hasStroke = false
    var transparent = false
    var radius = 0f
    var topLeftRadius = 0f
    var topRightRadius = 0f
    var bottomLeftRadius = 0f
    var bottomRightRadius = 0f
    var isGradient = false
    var gradientFirstColor = 0
    var gradientSecondColor = 0
    var gradientOrientation = GradientDrawable.Orientation.TOP_BOTTOM.ordinal
    var gradientRadius = 0f
    var hasRipple = true

    companion object {

        fun createFromTypedArray(context: Context, a: TypedArray): MeowShapeDrawable {
            val shapeDrawable = MeowShapeDrawable()
            shapeDrawable.shape = a.getInt(R.styleable.MeowShapeDrawable_msd_shape, GradientDrawable.RECTANGLE)
            shapeDrawable.mainColor = controller.onColorGet(context, a.getColor(R.styleable.MeowShapeDrawable_msd_mainColor, 0))
            shapeDrawable.mainColorAlpha = a.getFloat(R.styleable.MeowShapeDrawable_msd_mainColorAlpha, 1f)
            shapeDrawable.backgroundColor = controller.onColorGet(context, a.getColor(R.styleable.MeowShapeDrawable_msd_backgroundColor, 0))
            shapeDrawable.rippleColor = controller.onColorGet(context, a.getColor(R.styleable.MeowShapeDrawable_msd_rippleColor, context.getColorCompat(R.color.ripple_default)))
            shapeDrawable.rippleColorAlpha = a.getFloat(R.styleable.MeowShapeDrawable_msd_rippleColorAlpha, context.getFloat(R.dimen.ripple_default))
            shapeDrawable.hasStroke = a.getBoolean(R.styleable.MeowShapeDrawable_msd_hasStroke, false)
            shapeDrawable.strokeWidth = a.getDimension(R.styleable.MeowShapeDrawable_msd_strokeWidth, 0f)
            shapeDrawable.transparent = a.getBoolean(R.styleable.MeowShapeDrawable_msd_isTransparent, false)
            shapeDrawable.radius = a.getDimension(R.styleable.MeowShapeDrawable_msd_radius, 0f)
            shapeDrawable.topLeftRadius = a.getDimension(R.styleable.MeowShapeDrawable_msd_radius_topLeft, 0f)
            shapeDrawable.topRightRadius = a.getDimension(R.styleable.MeowShapeDrawable_msd_radius_topRight, 0f)
            shapeDrawable.bottomLeftRadius = a.getDimension(R.styleable.MeowShapeDrawable_msd_radius_bottomLeft, 0f)
            shapeDrawable.bottomRightRadius = a.getDimension(R.styleable.MeowShapeDrawable_msd_radius_bottomRight, 0f)
            shapeDrawable.isGradient = a.getBoolean(R.styleable.MeowShapeDrawable_msd_isGradient, false)
            shapeDrawable.gradientFirstColor = controller.onColorGet(context, a.getColor(R.styleable.MeowShapeDrawable_msd_gradientFirstColor, 0))
            shapeDrawable.gradientSecondColor = controller.onColorGet(context, a.getColor(R.styleable.MeowShapeDrawable_msd_gradientSecondColor, 0))
            shapeDrawable.gradientOrientation = a.getInt(R.styleable.MeowShapeDrawable_msd_gradientOrientation, GradientDrawable.Orientation.TOP_BOTTOM.ordinal)
            shapeDrawable.gradientRadius = a.getDimension(R.styleable.MeowShapeDrawable_msd_gradientRadius, 0f)
            shapeDrawable.hasRipple = a.getBoolean(R.styleable.MeowShapeDrawable_msd_hasRipple, true)
            return shapeDrawable
        }
    }
}

fun Context.getDrawableCompat(res: Int) = ContextCompat.getDrawable(this, res)

fun View?.changeBackgroundColor(color: Int) {
    if (this == null)
        return

    val d = background ?: return
    d.mutate()
    d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}