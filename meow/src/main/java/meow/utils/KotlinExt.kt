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

/**
 * Extensions of Kotlin.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

inline fun <reified T> createClass() = Class.forName(T::class.java.name) as Class<T>

fun <T> T.print(m: String = "") = apply {
    kotlin.io.print("$m ")
    print(this)
}

fun <T> T.println(m: String = "") = apply {
    kotlin.io.print("$m ")
    println(this)
}

fun Any.setPrivateField(name: String, value: Any) {
    avoidException {
        this.javaClass.getDeclaredField(name).apply {
            isAccessible = true
            set(this@setPrivateField, value)
        }
    }
}

fun <T> Any.getPrivateField(name: String): IntArray? =
    avoidException(
        tryBlock = {
            val filed = this.javaClass.getDeclaredField(name).apply {
                isAccessible = true
            }
            (filed.get(this@getPrivateField) as IntArray)
        },
        exceptionBlock = { null }
    )