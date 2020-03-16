@file:Suppress("unused")

package meow.util

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

/**
 * Created by 1HE on 9/15/2018.
 */

object DisplayHelper {

    var dp: Float = 0f

    fun init(context: Context) {
        dp = context.resources.displayMetrics.density
    }

    fun getActionBarHeight(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else 0
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getDisplaySize(context: Context?): Point {
        return avoidExceptionReturn({
            val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val p = Point()

            val realMetrics = DisplayMetrics()
            display.getRealMetrics(realMetrics)
            p.x = realMetrics.widthPixels
            p.y = realMetrics.heightPixels

            p
        }, { Point() })
    }

}

fun dipf(f: Float) = f * DisplayHelper.dp

fun dipf(i: Int) = i * DisplayHelper.dp

fun dip(i: Int) = (i * DisplayHelper.dp).toInt()

fun dip(f: Float) = (f * DisplayHelper.dp).toInt()

fun toDp(pixelSize: Float) = pixelSize / DisplayHelper.dp

