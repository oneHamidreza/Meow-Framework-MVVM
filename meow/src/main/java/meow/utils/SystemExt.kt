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

package meow.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import meow.controller

/**
 * The Extensions of OS & Build.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

fun getDeviceModel(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL ?: return manufacturer ?: "Unknown Device"
    return if (model.startsWith(manufacturer)) {
        model.capitalizeFirst()
    } else {
        manufacturer.capitalizeFirst() + " " + model
    }

}

/**
 * Get display width and height in pixel.
 * context is the Android Context and can be null. if it is null then returns a object from Point with zero values.
 * @return size of display in pixel (WxH) in a [Point] object.
 */
fun Context?.getDisplaySize(): Point {
    if (this == null)
        return Point(0, 0)

    return avoidException(
        tryBlock = {
            val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val p = Point()

            val realMetrics = DisplayMetrics()
            display.getRealMetrics(realMetrics)
            p.x = realMetrics.widthPixels
            p.y = realMetrics.heightPixels

            p
        },
        exceptionBlock = { Point() })!!
}


fun Float.toPx() = this * controller.dpi
fun Int.toPx() = this * controller.dpi.toInt()

fun Float.toDp() = this / controller.dpi
fun Int.toDp() = (this.toFloat() / controller.dpi).toInt()
