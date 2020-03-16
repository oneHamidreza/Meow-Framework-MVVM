@file:Suppress("unused")

package meow.util

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.core.graphics.ColorUtils


/**
 * Created by 1HE on 9/16/2018.
 */

object ColorHelper {

    fun mixTwoColors(color1: Int, color2: Int, amount: Float): Int {
        val alphaChannel = 24
        val redChannel = 16
        val greenChannel = 8

        val inverseAmount = 1.0f - amount

        val a = ((color1 shr alphaChannel and 0xff).toFloat() * amount + (color2 shr alphaChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val r = ((color1 shr redChannel and 0xff).toFloat() * amount + (color2 shr redChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val g = ((color1 shr greenChannel and 0xff).toFloat() * amount + (color2 shr greenChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val b = ((color1 and 0xff).toFloat() * amount + (color2 and 0xff).toFloat() * inverseAmount).toInt() and 0xff

        return a shl alphaChannel or (r shl redChannel) or (g shl greenChannel) or b
    }

    fun getLighterOrDarkerColor(color: Int, f: Float): Int {// dark : f < 1 , light : f > 1
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= f
        return Color.HSVToColor(hsv)
    }

    fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    fun setAlpha(color: Int, alpha: Float) = ColorUtils.setAlphaComponent(color, (alpha * 255).toInt())
}

//fun Context?.getColorCompat(resColor: Int): Int {
//    if (this == null) return 0
//    return controller.onColorGet(this, ContextCompat.getColor(this, resColor))
//}

fun Context?.getDimension(res: Int): Float {
    if (this == null) return 0f
    return this.resources.getDimension(res)
}

fun Context?.getFloat(res: Int): Float {
    if (this == null) return 0f
    val typedValue = TypedValue()
    resources.getValue(res, typedValue, true)
    return typedValue.float
}

fun Context?.getBoolean(res: Int): Boolean {
    if (this == null) return false
    return this.resources.getBoolean(res)
}

fun Context?.getInteger(res: Int): Int {
    if (this == null) return 0
    return this.resources.getInteger(res)
}