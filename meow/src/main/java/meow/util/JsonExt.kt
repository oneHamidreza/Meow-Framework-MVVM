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

package meow.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource

/**
 * Extensions of Json Data.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory())

inline fun <reified T> BufferedSource?.toClass(): T? {
    if (this == null) return null
    return avoidException {
        moshiBuilder.build().adapter(createClass<T>()).fromJson(this)
    }
}

inline fun <reified T> String?.toClass(): T? {
    if (this == null) return null
    return avoidException {
        moshiBuilder.build().adapter(createClass<T>()).fromJson(this)
    }
}

inline fun <reified T : Any> T?.toJsonString(): String {
    if (this == null) return "{}"
    return avoidException {
        moshiBuilder
            .add(KotlinJsonAdapterFactory())
            .build().adapter(createClass<T>()).toJson(this)
    } ?: "{}"
}

inline fun <reified T : Any> List<T>?.toJsonString(): String {
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

fun JsonReader.skipNameAndValue() {
    skipName()
    skipValue()
}

inline fun JsonReader.readObject(block: JsonReader.() -> Unit) {
    beginObject()
    while (hasNext()) {
        block()
    }
    endObject()
}


inline fun JsonReader.readArray(block: JsonReader.() -> Unit) {
    beginArray()
    while (hasNext()) {
        block()
    }
    endArray()
}

inline fun <reified T> JsonReader.safeNext() = avoidException {
    when (createClass<T>().javaClass.name) {
        "String" -> nextString() as? T
        else -> newInstance()
    }
}

fun JsonReader.selectName(vararg strings: String) = selectName(JsonReader.Options.of(*strings))

fun ofMoshi(factory: JsonAdapter.Factory? = null) =
    Moshi.Builder().apply {
        if (factory != null)
            add(factory)
        add(KotlinJsonAdapterFactory())
    }.build()
