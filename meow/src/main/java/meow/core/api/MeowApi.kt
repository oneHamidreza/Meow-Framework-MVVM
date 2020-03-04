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

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * The class of Api Utils such as OKHTTP client, Retrofit Configuration containing Moshi Adapter.
 * and containing Authorization methods.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-28
 */

open class MeowApi(
    val okHttpClient: OkHttpClient,
    val options: Options
) {

    //todo add cache
    //todo add authenticator

    fun createDefaultService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(options.baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> createServiceByAdapter(adapter: Any? = null): T {
        val moshiBuilder = Moshi.Builder()
        val moshi = adapter?.let {
            moshiBuilder.add(KotlinJsonAdapterFactory()).add(it).build()
        } ?: moshiBuilder.add(KotlinJsonAdapterFactory()).build()
        return createDefaultService().newBuilder()
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build().create(T::class.java)
    }

    sealed class Authorization(val interceptor: Interceptor) {
        class JWT(
            isLogin: Boolean,
            token: String
        ) : Authorization(Interceptor {
            val builder = it.request().newBuilder()
            if (isLogin)
                builder.addHeader("Authorization", "Bearer $token")
            it.proceed(builder.build())
        })
    }

    class Options(
        var baseUrl: String
    )
}