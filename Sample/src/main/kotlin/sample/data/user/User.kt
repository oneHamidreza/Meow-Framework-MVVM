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

package sample.data.user

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable
import meow.core.api.MeowOauthToken
import meow.util.removeExtraSpaces
import retrofit2.http.GET
import retrofit2.http.Query
import sample.data.DataSource

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

    val alias get() = (firstName.orEmpty() + " " + lastName.orEmpty() + " (" + id + ")").removeExtraSpaces()

    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName)"
    }

    class Repository(private val ds: DataSource) {

        suspend fun postLoginToApi(request: Api.RequestLogin) = ds.postLoginToApi(request)

    }

    interface Api {

        class RequestLogin(
            var username: String,
            var password: String
        )

        @GET("login")
        suspend fun login(
            @Query("username") username: String,
            @Query("password") password: String
        ): MeowOauthToken
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