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

import android.webkit.WebSettings
import meow.core.api.MeowApi
import meow.core.api.MeowLoggingInterceptor
import meow.core.controller
import meow.core.di.Injector
import okhttp3.OkHttpClient
import org.kodein.di.erased.instance
import sample.di.dataInjectors
import java.util.concurrent.TimeUnit

/**
 * The Api Client class inherits from [okhttp3.OkHttpClient].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

val userAgent by lazy {
    WebSettings.getDefaultUserAgent(Injector.context()).replace(Regex("[^A-Za-z0-9 ().,_/]"), "")
}

val okHttpClient = OkHttpClient.Builder().apply {
    connectTimeout(30, TimeUnit.SECONDS)
    readTimeout(60, TimeUnit.SECONDS)
    writeTimeout(60, TimeUnit.SECONDS)

    val isLogin by dataInjectors.instance<Boolean>("isLogin")
    if (isLogin)
        addInterceptor(MeowApi.Authorization.JWT(isLogin, "xxx").interceptor)

    addInterceptor {
        it.proceed(
            it.request().newBuilder()
                .addHeader("User-Agent", userAgent)
                .build()
        )
    }

    if (controller.isDebugMode)
        addInterceptor(MeowLoggingInterceptor())
}.build()