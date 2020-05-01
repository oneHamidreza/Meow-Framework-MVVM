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

import meow.ktx.logD
import okhttp3.Interceptor
import okhttp3.Response

/**
 * The class of Http Logging Interceptor inherits from [Interceptor].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-04
 */

class MeowLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        logD(
            "OkHttp",
            "Request Connection >>  ${request.method()}  ${request.url()} isHTTPS = ${request.isHttps}\n"
                    + "Request Headers >> ${request.headers()}\n"
                    + if (request.body() != null) "Request Body >> ${request.body()}" else ""
        )

        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        val body = response.peekBody(1024 * 1024)
        logD(
            "OkHttp",
            "Response >>  ${request.method()}  ${response.request()
                .url()} isHTTPS = ${request.isHttps} \n in ${((t2 - t1) / 1e6).toInt()} ms\n"
                    + "Response Headers >> ${response.headers()}\n"
                    + if (response.body() != null) "Response Body >> ${body.string()}" else ""
        )

        return response
    }

}