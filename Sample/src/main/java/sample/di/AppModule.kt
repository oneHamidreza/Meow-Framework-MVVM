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

package sample.di

import meow.core.data.MeowSharedPreferences
import org.kodein.di.Kodein.Module
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import sample.BuildConfig
import sample.data.DataSource

/**
 * The Module of application (resources, shared preferences).
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

val appModule = Module("App Module", false) {
    bind("apiUrl") from singleton { "BuildConfig" }
    bind() from singleton { DataSource(instance()) }
    bind("spMain") from singleton {
        MeowSharedPreferences(
            instance(),
            "mainSettings_v1"
        )
    }
    bind("spUpdate") from singleton {
        MeowSharedPreferences(
            instance(),
            "updateSettings_v" + BuildConfig.VERSION_CODE
        )
    }
}