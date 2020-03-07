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

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable
import meow.core.api.MeowRequest
import meow.core.arch.MeowRepository
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * User Model class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

class User {

    @Serializable
    data class Model(
        @Json(name = "id") var id: String? = null,
        @Json(name = "first_name") var firstName: String? = null,
        @Json(name = "last_name") var lastName: String? = null
    ) {

        val alias: String
            get() {
                return firstName.orEmpty() + " " + lastName.orEmpty() + " (" + id + ")"
            }//todo remove extra

        override fun toString(): String {
            return "Model(id=$id, firstName=$firstName, lastName=$lastName)"
        }
    }

    @Serializable
    data class RequestGet(
        @Json(name = "id") var id: String? = null
    ) : MeowRequest {

        override fun validate(): Boolean {
            return id != null
        }
    }

    class Repository(private val ds: DataSource) : MeowRepository() {

        suspend fun getUserByIdApi(request: RequestGet) = ds.getUserById(request)
        fun getSavedUser() = ds.fetchUser()
        fun saveUser(it: Model) = ds.saveUser(it)
    }

    interface Api {
        @GET("user/{id}")
        suspend fun getUserById(@Path("id") id: String?): Model
    }

}