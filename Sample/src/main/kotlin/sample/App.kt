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

package sample

import android.app.Application
import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import meow.MeowApp
import meow.MeowController
import meow.controller
import meow.ktx.MeowCurrency
import meow.ktx.getFontCompat
import meow.ktx.isNightModeFromSettings
import meow.meowModule
import org.kodein.di.Kodein
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import sample.data.DataSource
import sample.di.apiModule
import sample.di.appModule
import sample.di.mvvmModule

/**
 * Sample Application class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

class App : MeowApp() {

    override val kodein = Kodein.lazy {
        bind() from singleton { kodein.direct }
        bind() from singleton { this@App }
        import(androidXModule(this@App))
        import(meowModule)
        import(appModule)
        import(apiModule)
        import(mvvmModule)
    }

    val dataSource by instance<DataSource>()

    override fun getLanguage(context: Context?) = "en"
    override fun getTheme(context: Context?) =
        if (context.isNightModeFromSettings()) MeowController.Theme.NIGHT else MeowController.Theme.DAY

    override fun onCreate() {
        super.onCreate()
        MeowController().apply {
            isDebugMode = BuildConfig.DEBUG
            isLogTagNative = false

            language = getLanguage(this@App)
            currency = MeowCurrency.USD
            theme = getTheme(this@App)

            defaultTypefaceResId = if (isPersian) R.font.farsi_regular else R.font.english_regular
            toastTypefaceResId = if (isPersian) R.font.farsi_regular else R.font.english_regular

            onException = {
                // Log to Fabric or any other Crash Management System. Just use `avoidException` instead of `try{}catch{}`
            }
        }.bindApp(this)
    }

}

@GlideModule
class AppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setLogLevel(Log.ERROR)
    }
}

fun Application.getDefaultTypeface() =
    getFontCompat(if (controller.isPersian) R.font.farsi_regular else R.font.english_regular)
