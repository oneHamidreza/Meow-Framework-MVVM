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

package meow.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource

/**
 * The Extensions of Json Data.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory())

inline fun <reified T> BufferedSource?.fetchByClass(): T? {
    if (this == null) return null
    return try {
        moshiBuilder.build().adapter(createClass<T>()).fromJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T> String?.fetchByClass(): T? {
    if (this == null) return null
    return try {
        moshiBuilder.build().adapter(createClass<T>()).fromJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T : Any> T?.toJson(): String {
    if (this == null) return "{}"
    return try {
        moshiBuilder
            .add(KotlinJsonAdapterFactory())
            .build().adapter(createClass<T>()).toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        "{}"
    }
}

inline fun <reified T : Any> List<T>?.toJson(): String {
    if (this == null) return "[]"
    return try {
        moshiBuilder
            .add(KotlinJsonAdapterFactory())
            .build().adapter(createClass<List<T>>()).toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        "[]"
    }
}
 