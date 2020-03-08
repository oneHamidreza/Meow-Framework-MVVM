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

import androidx.recyclerview.widget.DiffUtil
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

@Serializable
data class User(
    @Json(name = "id") var id: String? = null,
    @Json(name = "first_name") var firstName: String? = null,
    @Json(name = "last_name") var lastName: String? = null
) {

    val alias: String
        get() {
            return firstName.orEmpty() + " " + lastName.orEmpty() + " (" + id + ")"
        }//todo remove extra

    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName)"
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
        suspend fun getUsersApi() = ds.getUsers()

        fun getSavedUser() = ds.fetchUser()
        fun saveUser(it: User) = ds.saveUser(it)
    }

    interface Api {
        @GET("user/{id}")
        suspend fun getUserById(@Path("id") id: String?): User

        @GET("users")
        suspend fun getUsers(): List<User>
    }


    /**
     * Callback for calculating the diff between two non-null items in a list.
     * https://github.com/android/architecture-samples
     *
     * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem

    }


}