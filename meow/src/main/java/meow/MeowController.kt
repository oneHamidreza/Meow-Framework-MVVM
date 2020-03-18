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

package meow

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Build
import android.util.LayoutDirection
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import meow.core.api.MeowSession
import meow.util.MeowCurrency
import meow.util.getPrivateField
import meow.util.isNightModeFromSettings
import meow.util.setPrivateField
import java.util.*

/**
 * 🐈 This CAT can control configurations and UI properties in one Application. Just trust it.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

lateinit var controller: MeowController

class MeowController(
    val app: Application,
    val meowSession: MeowSession = MeowSession(),
    var isDebugMode: Boolean = true,
    var isLogTagNative: Boolean = true,
    var apiSuccessRange: IntRange = 200..200,
    var onException: (exception: Exception) -> Unit = {},
    var dpi: Float = app.resources.displayMetrics.density,
    var layoutDirection: Int = LayoutDirection.INHERIT,
    var language: String = "en",
    var currency: MeowCurrency = MeowCurrency.USD,
    var rootFolderName: String = "meow",
    var onColorGet: (color: Int) -> Int = { color -> color },
    internal var onColorStateListGet: (colorStateList: ColorStateList) -> ColorStateList = { color ->
        color.apply {
            val colors: IntArray = getPrivateField<Array<Int>>("mColors")!!
            colors.forEachIndexed { index, it ->
                colors[index] = onColorGet(it)
            }
            setPrivateField("mDefaultColor", onColorGet(defaultColor))
            setPrivateField("mColors", colors)
        }
        ColorStateList.valueOf(onColorGet(color.defaultColor))
    },
    var defaultFontName: String = "",
    var isForceFontPadding: Boolean = false,
    var isPersian: Boolean = false,
    var changeColor: Boolean = false,
    forceNightMode: Boolean = false
) {

    var theme = if (app.isNightModeFromSettings() || forceNightMode) Theme.NIGHT else Theme.DAY
        set(value) {
            field = value
            val nightMode = when (value) {
                Theme.DAY -> AppCompatDelegate.MODE_NIGHT_NO
                Theme.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

    val isNightMode
        get() = theme == Theme.NIGHT

    val isRtl
        get() = layoutDirection == LayoutDirection.RTL

    fun init() {
        controller = this
    }

    fun swapTheme() {
        theme = if (isNightMode) Theme.DAY else Theme.NIGHT
    }

    fun updateLanguage(activity: FragmentActivity, language: String) {
        this.language = language
        activity.recreate()
    }

    enum class Theme {
        DAY, NIGHT
    }

    @SuppressLint("ObsoleteSdkInt")
    fun wrap(contextParam: Context?): ContextWrapper? {
        if (contextParam == null)
            return null

        var context: Context = contextParam
        val config = contextParam.resources.configuration
        if (language.isNotEmpty()) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale)
            } else {
                setSystemLocaleLegacy(config, locale)
            }
            config.setLayoutDirection(locale)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(config, app.resources.displayMetrics)
        }
        return ContextWrapper(context)
    }

    private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun setSystemLocale(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }
}