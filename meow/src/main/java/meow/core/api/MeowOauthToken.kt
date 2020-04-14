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

package meow.core.api

import android.annotation.SuppressLint
import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * The class of Api Utils such as OKHTTP client, Retrofit Configuration containing Moshi Adapter.
 * and containing Authorization methods.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-28
 */

class MeowOauthToken {

    @Json(name = "access_token")
    var accessToken: String? = null

    @Json(name = "token")
    var token: String? = null

    @Json(name = "expires_in")
    var expiresIn: Long = 0L

    @Json(name = "token_type")
    var tokenType: String? = null
        @SuppressLint("DefaultLocale")
        set(value) {
            if (value == null) {
                field = ""
                return
            }
            field = if (!Character.isUpperCase(value[0]))
                value[0].toString().toUpperCase() + value.substring(1)
            else
                value
        }

    @Json(name = "scope")
    var scope: String? = null

    @Json(name = "refresh_token")
    var refreshToken: String? = null

    val isLoginSuccess: Boolean
        get() {
            return accessToken?.isNotEmpty() ?: false
        }

    override fun toString(): String {
        return "MeowOauthToken{" +
                "accessToken='" + accessToken + '\''.toString() +
                ", expiresIn=" + expiresIn +
                ", tokenType='" + tokenType + '\''.toString() +
                ", scope='" + scope + '\''.toString() +
                ", refreshToken='" + refreshToken + '\''.toString() +
                '}'.toString()
    }

    class RequestRefreshToken(
        @Json(name = "refresh_token") var refreshToken: String,
        session: MeowSession
    ) : RequestPrimary(session, "refresh_token")

    open class RequestPrimary(
        session: MeowSession,
        @property:Json(name = "grant_type") var grantType: String? = null
    ) {

        @Json(name = "client_id")
        var clientId: String? = "public-android"

        @Json(name = "uuid")
        var uuid: String? = session.uuid

        @Json(name = "pid")
        var pid: String? = session.pushNotificationToken

        @Json(name = "os")
        var os: String? = session.deviceOS

        @Json(name = "os_version")
        var osVersion: Int? = session.deviceOSVersionCode

        @Json(name = "phone_model")
        var phoneModel: String? = session.deviceModel

        @Json(name = "app_version")
        var appVersion: Int? = session.appVersionCode
    }

    interface Api {
        @POST("/api/oauth2/token")
        fun refreshToken(
            @Body request: RequestRefreshToken
        ): MeowOauthToken

    }

}
