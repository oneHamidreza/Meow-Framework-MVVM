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

import meow.controller
import meow.core.api.*
import okhttp3.OkHttpClient
import org.kodein.di.Kodein.Module
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import sample.App
import sample.data.DataSource

/**
 * The Module of application (resources, shared preferences).
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

val apiModule = Module("Network Module", false) {
    bind() from provider { AppApi(instance(), "refreshTokenValue") }
}

fun createOkHttpClient(api: AppApi, options: MeowApi.Options, dataSource: DataSource) =
    api.app.getMeowClientBuilder().apply {
        val isLogin = dataSource.isLogin()
        val authorization = MeowApi.Authorization.SimpleToken(isLogin, "xxx")

        val interceptorBlocks: List<InterceptorBlock> = listOf(
            api.app.getCacheInterceptorBlock(options),
            authorization.interceptorBlock,
            { it.header("User-Agent", api.app.getUserAgent()) }
        )
        addInterceptorBlocks(interceptorBlocks)

        authenticator(MeowApi.RefreshToken(api, authorization))

    }.build()

open class AppApi(
    open var app: App,
    open var refreshToken: String? = null,
    override var options: Options = Options()
) : MeowApi(options) {

    override fun getRefreshTokenResponse(): Response<MeowOauthToken>? {
        if (refreshToken == null) return null
        val request = MeowOauthToken.RequestRefreshToken(refreshToken!!, controller.meowSession)
        return Response.success(createServiceByAdapter<Oauth>().refreshToken(request))
    }

    override fun getOkHttpClient(): OkHttpClient {
        return createOkHttpClient(this, options, DataSource(app))
    }

    override fun getBaseUrl(): String {
        return "http://192.168.1.10:8888"
    }

    interface Oauth {
        @POST("/api/oauth2/token")
        fun refreshToken(
            @Body request: MeowOauthToken.RequestRefreshToken
        ): MeowOauthToken

    }
}

class TestAppApi(
    override var app: App,
    override var refreshToken: String? = null,
    override var options: Options = Options()
) : AppApi(app, refreshToken, options) {
    override fun getBaseUrl(): String {
        return "test-api.com"
    }
}