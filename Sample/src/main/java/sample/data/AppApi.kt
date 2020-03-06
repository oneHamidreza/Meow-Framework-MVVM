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

import meow.core.api.*
import meow.core.controller
import meow.core.di.Injector
import meow.utils.hasNetwork
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.kodein.di.erased.instance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import sample.di.dataInjectors
import java.util.concurrent.TimeUnit

/**
 * The Api of Application class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

/*
    Optimize OkHttp For Caching
    https://medium.com/@bapspatil/caching-with-retrofit-store-responses-offline-71439ed32fda
*/

fun createOkHttpClient(api: AppApi, options: MeowApi.Options) = meowClientBuilder.apply {
    val isLogin by dataInjectors.instance<Boolean>("isLogin")
    val authorization = MeowApi.Authorization.SimpleToken(isLogin, "xxx")

    val interceptorBlocks: List<InterceptorBlock> = listOf(
        getCacheInterceptorBlock(options),
        authorization.interceptorBlock,
        { it.header("User-Agent", userAgent) }
    )
    addBuilderBlocks(interceptorBlocks)

    authenticator(MeowApi.RefreshToken(api, authorization))

}.build()

class AppApi(
    var refreshToken: String? = null,
    override var options: Options = Options()
) : MeowApi(options) {

    override fun getRefreshTokenResponse(): Response<MeowOauthToken>? {
        if (refreshToken == null) return null
        val request = MeowOauthToken.RequestRefreshToken(refreshToken!!, controller.meowSession)
        return Response.success(createServiceByAdapter<Oauth>().refreshToken(request))
    }

    override fun getOkHttpClient(): OkHttpClient {
        return createOkHttpClient(this, options)
    }

    override fun getBaseUrl(): String {
        return "http://192.168.1.10:8080"
    }

    interface Oauth {
        @POST("/api/oauth2/token")
        fun refreshToken(
            @Body request: MeowOauthToken.RequestRefreshToken
        ): MeowOauthToken

    }
}

