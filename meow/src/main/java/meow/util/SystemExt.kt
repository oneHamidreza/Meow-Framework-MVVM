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

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.etebarian.meowframework.R
import meow.controller
import meow.core.ui.MeowFragment

/**
 * Extensions of OS & Build.
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

fun Context?.isPackageInstalled(packageName: String): Boolean {
    if (this == null)
        return false

    return avoidException {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } ?: false
}

fun MeowFragment<*, *>?.isPackageInstalled(packageName: String) =
    this?.context().isPackageInstalled(packageName)

@Suppress("DEPRECATION")
@SuppressLint("HardwareIds", "MissingPermission")
fun Context?.getIMEI(): String {
    if (this == null)
        return "0"

    return avoidException {
        (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    } ?: "0"
}

fun MeowFragment<*, *>?.getIMEI() = this?.context().getIMEI()

@SuppressLint("HardwareIds", "MissingPermission", "NewApi")
fun Context?.getPhoneNumber(): String? {
    if (this == null)
        return null
    return avoidException {
        sdkNeedRType<String>(23) {
            val subscription =
                getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            val list = subscription.activeSubscriptionInfoList
            list.forEach {
                if (it.number.isNotEmpty())
                    return it.number
            }
            return null
        }
        (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number
    }
}

@SuppressLint("DefaultLocale")
fun Context?.getCountryCode(): String {
    if (this == null)
        return ""

    var countryID: String
    var countryZipCode = ""
    avoidException {
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        countryID = manager.simCountryIso.toUpperCase()
        val rl = resources.getStringArray(R.array.countryCodes)
        for (aRl in rl) {
            val g = aRl.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (g[1].trim { it <= ' ' } == countryID.trim { it <= ' ' }) {
                countryZipCode = g[0]
                break
            }
        }
    }
    return countryZipCode
}

/**
 * Get display width and height in pixel.
 * context is the Android Context and can be null. if it is null then returns a object from Point with zero values.
 * @return size of display in pixel (WxH) in a [Point] object.
 */
fun Context?.getDisplaySize(): Point {
    if (this == null)
        return Point(0, 0)

    return avoidException {
            val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val p = Point()

            val realMetrics = DisplayMetrics()
            display.getRealMetrics(realMetrics)
            p.x = realMetrics.widthPixels
            p.y = realMetrics.heightPixels

            p
    } ?: Point()
}

/**
 * Get Toolbar height in pixel.
 * @param context is the Android Context and can be null. if it is null then returns 0.
 * @return Height of action bar in pixel.
 */
fun Context?.getToolbarHeight(): Int {
    if (this == null)
        return 0
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else 0
}

/**
 * Get Status Bar height in pixel.
 * @param context is the Android Context and can be null. if it is null then returns 1.
 * @return Height of status bar in pixel.
 */
fun Context?.getStatusBarHeight(): Int {
    if (this == null)
        return 0
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        result = resources.getDimensionPixelSize(resourceId)
    return result
}

fun Float.dp() = this * controller.dpi
fun Int.dp() = this * controller.dpi.toInt()

fun Float.px() = this / controller.dpi
fun Int.px() = (this.toFloat() / controller.dpi).toInt()

fun Context?.isNightModeFromSettings(): Boolean {
    if (this == null) return false
    val uiModeManager = ContextCompat.getSystemService(this, UiModeManager::class.java)
    return uiModeManager?.nightMode != UiModeManager.MODE_NIGHT_NO
}

fun Context?.showOrHideKeyboard(show: Boolean) {
    if (this == null)
        return

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (show)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    else
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
fun Context.vibrate(duration: Long = 150) {
    avoidException {
        val vibrator = getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        if (sdkNeedReturn(26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            })
        else
            vibrator.vibrate(longArrayOf(0, duration), -1)
    }
}