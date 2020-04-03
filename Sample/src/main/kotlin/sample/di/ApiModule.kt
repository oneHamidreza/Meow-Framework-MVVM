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
import meow.core.api.InterceptorBlock
import meow.core.api.MeowApi
import meow.core.api.MeowOauthToken
import meow.core.api.addInterceptorBlocks
import okhttp3.OkHttpClient
import org.kodein.di.Kodein.Module
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import sample.App
import sample.BuildConfig
import sample.data.DataSource

/**
 * The Module of application (resources, shared preferences).
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

val apiModule = Module("Api Module", false) {
    bind() from provider {
        AppApi(
            app = instance(),
            dataSource = instance()
        )
    }
}

open class AppApi(
    open val app: App,
    open val dataSource: DataSource,
    override var options: Options = Options(),
    override var baseUrl: String = BuildConfig.API_URL
) : MeowApi(baseUrl = baseUrl, options = options) {

    override fun getRefreshTokenResponse(): Response<MeowOauthToken>? {
        val refreshToken = dataSource.fetchApiRefreshToken()
        if (refreshToken.isEmpty()) return null

        val request = MeowOauthToken.RequestRefreshToken(refreshToken, controller.meowSession)
        return Response.success(createServiceByAdapter<Oauth>().refreshToken(request))
    }

    override fun getOKHttpClientBuilder(): OkHttpClient.Builder {
        return super.getOKHttpClientBuilder().apply {
            val isLogin = dataSource.isLogin()
            val authorization = Authorization.Bearer(isLogin, dataSource.fetchApiToken())

            val interceptorBlocks: List<InterceptorBlock> = listOf(
                getCacheInterceptorBlock(app, options),
                authorization.interceptorBlock,
                { it.header("User-Agent", getDefaultUserAgent(app)) }
            )
            addInterceptorBlocks(interceptorBlocks)

            authenticator(OauthRefreshToken(this@AppApi, authorization))
        }
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
    override var dataSource: DataSource,
    override var options: Options = Options(),
    override var baseUrl: String = "test-api.yoururl.com"
) : AppApi(app, dataSource, options)
