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

package sample.data

import meow.core.data.MeowSharedPreferences
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.erased.instance
import sample.App
import sample.di.AppApi

/**
 * The Data Source class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

class DataSource(var app: App) : KodeinAware {

    override val kodein by closestKodein(app)
    val api: AppApi by instance()
    val spMain: MeowSharedPreferences by instance("spMain")
    val spUpdate: MeowSharedPreferences by instance("spUpdate")

    suspend fun getUserById(request: User.RequestGet) =
        api.createServiceByAdapter<User.Api>().getUserById(request.id)

    fun x() = "xxx"

    fun fetchTest() = spMain.get("user", "xxx")

    fun fetchUser() = spMain.get("user", User.Model())
    fun saveUser(it: User.Model) = spMain.put("user", it)
}