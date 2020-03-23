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
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.*

/**
 * Context Wrapper Utils class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-23
 */

object ContextWrapperUtils {

    @SuppressLint("ObsoleteSdkInt")
    fun wrap(contextParam: Context?, language: String): ContextWrapper? {
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
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
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