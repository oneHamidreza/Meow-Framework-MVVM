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

import android.content.res.Resources
import meow.core.arch.DataSourceImpl
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

class DataSource(override var app: App) : DataSourceImpl, KodeinAware {

    override val kodein by closestKodein(app)
    val api: AppApi by instance()
    val resources: Resources by instance()
    val spMain: MeowSharedPreferences by instance("spMain")
    val spUpdate: MeowSharedPreferences by instance("spUpdate")

    suspend fun getUserById(request: User.RequestGet) =
        api.createServiceByAdapter<User.Api>().getUserById(request.id)

    suspend fun getUsers() =
        api.createServiceByAdapter<User.Api>().getUsers()

    fun isLogin() = false
    fun fetchUser() = spMain.get("user", User())
    fun saveUser(it: User) = spMain.put("user", it)
}