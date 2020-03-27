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

import android.content.Context
import android.graphics.Color
import meow.MeowApp
import meow.MeowController
import meow.meowModule
import meow.util.toHexString
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
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

class App : MeowApp(), KodeinAware {

    override val kodein = Kodein.lazy {
        bind() from singleton { kodein.direct }
        bind() from singleton { this@App }
        import(androidXModule(this@App))
        import(meowModule)
        import(appModule)
        import(apiModule)
        import(mvvmModule)
    }

    override fun getLanguage(context: Context?) = "fa"

    override fun onCreate() {
        super.onCreate()
        MeowController().apply {
            onColorGet = {
                when (it.toHexString().toLowerCase()) {//todo
                    "#1cb3c8" -> Color.RED
                    else -> it
                }
            }
            language = getLanguage(this@App)
            isDebugMode = BuildConfig.DEBUG
            isLogTagNative = true
            defaultTypefaceResId = R.font.iransans_mobile_regular
            toastTypefaceResId = R.font.iransans_mobile_regular
            isPersian = true
            theme = MeowController.Theme.DAY
        }.bindApp(this)
    }

}
