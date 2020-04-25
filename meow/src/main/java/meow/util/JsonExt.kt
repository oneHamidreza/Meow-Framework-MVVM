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
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource
import java.lang.reflect.Type
import kotlin.reflect.full.primaryConstructor

/**
 * Extensions of Json Data.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

inline fun <reified T> BufferedSource?.fromJson(): T? {
    if (this == null) return null
    return avoidException {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(javaClass<T>())
            .fromJson(this)
    }
}

inline fun <reified T> String?.fromJson(): T? {
    if (this == null) return null
    return avoidException {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(javaClass<T>())
            .fromJson(this)
    }
}

inline fun <reified T> BufferedSource?.fromJsonList(): List<T>? {
    if (this == null) return null
    return avoidException {
        val type = Types.newParameterizedType(javaClass<List<*>>(), javaClass<T>())
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<T>>(type)
            .fromJson(this)
    }
}

inline fun <reified T> String?.fromJsonList(): List<T>? {
    if (this == null) return null
    return avoidException {
        val type = Types.newParameterizedType(javaClass<List<*>>(), javaClass<T>())
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<T>>(type)
            .fromJson(this)
    }
}

inline fun <reified T> T?.toJson(): String {
    if (this == null) return "{}"
    return avoidException {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(javaClass<T>()).toJson(this)
    } ?: "{}"
}

inline fun <reified T : Any> List<T>?.toJson(): String {
    if (this == null) return "[]"
    return avoidException {
        val type = Types.newParameterizedType(javaClass<List<*>>(), javaClass<T>())
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<T>>(type).toJson(this)
    } ?: "[]"
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

fun JsonReader.selectName(vararg strings: String) = selectName(JsonReader.Options.of(*strings))

fun ofMoshi(factory: JsonAdapter.Factory? = null) =
    Moshi.Builder().apply {
        if (factory != null)
            add(factory)
        add(KotlinJsonAdapterFactory())
    }.build()

inline fun <reified T, reified JA : JsonAdapter<T>> ofMoshiUseAdapter() =
    ofMoshi(ModelJsonFactory().get<T, JA>())

class ModelJsonFactory {
    inline fun <reified T, reified JA : JsonAdapter<T>> get() = object : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (annotations.isNotEmpty()) return null
            if (Types.getRawType(type) == javaClass<T>()) {
                val delegate = moshi.nextAdapter<T>(this, type, annotations)
                return JA::class.primaryConstructor?.call(delegate)
            }
            return null
        }
    }
}