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

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import meow.core.arch.MeowViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import java.util.*

/**
 * The Base of Application class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-08
 */

val meowModule = Kodein.Module("Meow Module", false) {
    bind<ViewModelProvider.Factory>() with singleton {
        MeowViewModelFactory(
            kodein.direct
        )
    }
}

abstract class MeowApp : Application(), KodeinAware {

    private var localizationDelegate = LocalizationApplicationDelegate()

    open fun getLanguage(context: Context?) = ""
    open fun getTheme(context: Context?) = MeowController.Theme.DAY

    override fun attachBaseContext(newBase: Context?) {
        val language = getLanguage(newBase)
        if (language.isEmpty())
            return

        localizationDelegate.setDefaultLanguage(newBase!!, Locale(language))
        val context = localizationDelegate.attachBaseContext(newBase)
        super.attachBaseContext(context)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

}