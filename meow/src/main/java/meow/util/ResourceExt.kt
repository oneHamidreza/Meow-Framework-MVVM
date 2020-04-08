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

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import meow.controller
import meow.core.ui.MeowFragment

/**
 * Extensions of [Resources] class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-07
 */

object MeowColorUtils {

    /**
     * Mix two colors and create new color by specified amount.
     * @param color1 is first color in integer type.
     * @param color2 is second color in integer type.
     * @param amount is value for mixing two colors between 0 to 1.
     * @return the mixed color.
     */
    fun mixTwoColors(
        @ColorInt color1: Int,
        @ColorInt color2: Int,
        @FloatRange(from = 0.0, to = 1.0) amount: Float
    ): Int {
        if (amount !in 0f..1f)
            throw IllegalArgumentException("The value of amount must be in range of 0 to 1")

        val alphaChannel = 24
        val redChannel = 16
        val greenChannel = 8

        val inverseAmount = 1.0f - amount

        val a =
            ((color1 shr alphaChannel and 0xff).toFloat() * amount + (color2 shr alphaChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val r =
            ((color1 shr redChannel and 0xff).toFloat() * amount + (color2 shr redChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val g =
            ((color1 shr greenChannel and 0xff).toFloat() * amount + (color2 shr greenChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val b =
            ((color1 and 0xff).toFloat() * amount + (color2 and 0xff).toFloat() * inverseAmount).toInt() and 0xff

        return a shl alphaChannel or (r shl redChannel) or (g shl greenChannel) or b
    }

    /**
     * Get lighter or darker from a color by specified amount.
     * @param color is the color in integer type.
     * @param amount is a float that describes the state of lighter or darker function. choose value lower than 1 to get darker color and choose value higher than 1 to get lighter color.
     * @return the lighter or darker color.
     */
    fun getLighterOrDarkerColor(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) amount: Float
    ): Int {// dark : f < 1 , light : f > 1
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= amount
        return Color.HSVToColor(hsv)
    }

    /**
     * Check the color is dark or light.
     * @param color is the color in integer type.
     * @param amount is a float that describes the state of lighter or darker function. choose value lower than 1 to get darker color and choose value higher than 1 to get lighter color.
     * @return the lighter or darker color.
     */
    fun isColorDark(@ColorInt color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    /**
     * Change alpha from color.
     * @param color is the color in integer type.
     * @param alpha is a float that describes value of transparency of color. Choose value between 0 and 1.
     */
    fun setAlpha(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) =
        ColorUtils.setAlphaComponent(color, (alpha * 255).toInt())

}

fun Int.toHexString() = String.format("#%06X", (0xFFFFFF and this))

fun Context.resources() = resources

fun Resources?.getDrawableCompat(
    @DrawableRes resId: Int,
    theme: Resources.Theme? = null
) =
    if (this == null) ColorDrawable(Color.TRANSPARENT) else ResourcesCompat.getDrawable(
        this,
        resId,
        theme
    )

fun Context?.getDrawableCompat(
    @DrawableRes resId: Int,
    theme: Resources.Theme? = null
) =
    this?.resources().getDrawableCompat(resId, theme)

fun MeowFragment<*>?.getDrawableCompat(
    @DrawableRes resId: Int,
    theme: Resources.Theme? = null
) =
    this?.resources().getDrawableCompat(resId, theme)

fun Context?.getFontCompat(@FontRes resId: Int = 0) =
    if (this == null || resId == 0) Typeface.DEFAULT else avoidException {
        ResourcesCompat.getFont(
            this,
            resId
        )
    } ?: Typeface.DEFAULT

fun MeowFragment<*>?.getFontCompat(@FontRes resId: Int = 0) =
    this?.context().getFontCompat(resId)

fun TypedArray.getColorCompat(index: Int, defValue: Int) =
    controller.onColorGet(getColor(index, defValue))

fun Resources?.getColorCompat(
    @ColorRes resId: Int,
    theme: Resources.Theme? = null
) = when {
    this == null -> 0
    else -> controller.onColorGet(ResourcesCompat.getColor(this, resId, theme))
}

fun Context?.getColorCompat(
    @ColorRes resId: Int,
    theme: Resources.Theme? = this?.theme
) = this?.resources().getColorCompat(resId, theme)

fun MeowFragment<*>?.getColorCompat(
    @ColorRes resId: Int,
    theme: Resources.Theme? = null
) = this?.resources().getColorCompat(resId, theme)

fun Resources?.getDimensionToPx(@DimenRes resId: Int) = this?.getDimension(resId)?.toInt() ?: 0
fun Context?.getDimensionToPx(@DimenRes resId: Int) = this?.resources().getDimensionToPx(resId)
fun MeowFragment<*>?.getDimensionToPx(@DimenRes resId: Int) =
    this?.resources().getDimensionToPx(resId)

fun Resources?.getFloatCompat(@DimenRes resId: Int) =
    if (this == null) 0f else ResourcesCompat.getFloat(this, resId)
fun Context?.getFloatCompat(@DimenRes resId: Int) = this?.resources().getFloatCompat(resId)
fun MeowFragment<*>?.getFloatCompat(@DimenRes resId: Int) =
    this?.resources().getFloatCompat(resId)

fun Resources?.getIntCompat(@IntegerRes resId: Int) = this?.getInteger(resId) ?: 0
fun Context?.getIntCompat(@IntegerRes resId: Int) = this?.resources().getIntCompat(resId)
fun MeowFragment<*>?.getIntCompat(@IntegerRes resId: Int) = this?.resources().getIntCompat(resId)

fun Resources?.getBooleanCompat(@BoolRes resId: Int) = this?.getBoolean(resId) ?: false
fun Context?.getBooleanCompat(@BoolRes resId: Int) = this?.resources().getBooleanCompat(resId)
fun MeowFragment<*>?.getBooleanCompat(@BoolRes resId: Int) =
    this?.resources().getBooleanCompat(resId)

fun Resources?.getStringArrayCompat(@ArrayRes resId: Int) =
    this?.getStringArray(resId) ?: emptyArray()

fun Context?.getStringArrayCompat(@ArrayRes resId: Int) =
    this?.resources().getStringArrayCompat(resId)

fun MeowFragment<*>?.getStringArrayCompat(@ArrayRes resId: Int) =
    this?.resources().getStringArrayCompat(resId)

fun Drawable?.setTintCompat(@ColorInt color: Int): Drawable {
    if (this == null) return ColorDrawable(Color.TRANSPARENT)
    return this.apply { DrawableCompat.setTint(this, color) }
}

fun Drawable.flip(): Drawable {
    val arD = arrayOf(this)
    return object : LayerDrawable(arD) {
        override fun draw(canvas: Canvas) {
            canvas.save()
            canvas.scale(-1f, 1f, (bounds.width() / 2).toFloat(), (bounds.width() / 2).toFloat())
            super.draw(canvas)
            canvas.restore()
        }
    }
}

fun Drawable.toBitmap(): Bitmap {
    val bitmap: Bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    if (this is BitmapDrawable)
        return bitmap

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}