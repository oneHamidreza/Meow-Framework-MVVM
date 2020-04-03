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

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Extensions of Kotlin.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

inline fun <reified T> javaClass() = T::class.java

inline fun <reified B, T> B.setField(name: String, value: T, useSuperClass: Boolean = false) =
    avoidException {
        val clazz = if (useSuperClass) javaClass<B>().superclass else javaClass<B>()
        val field = clazz?.getDeclaredField(name)?.apply {
            isAccessible = true
        }
        field?.set(this@setField, value)
        value
    }

inline fun <reified B, T> B.getField(name: String, useSuperClass: Boolean = false) =
    avoidException {
        val clazz = if (useSuperClass) javaClass<B>().superclass else javaClass<B>()
        val filed = clazz?.getDeclaredField(name)?.apply {
            isAccessible = true
        }
        @Suppress("UNCHECKED_CAST")
        (filed?.get(this@getField) as? T)
    }

fun launchSilent(
    context: CoroutineContext = Dispatchers.IO,
    exceptionHandler: CoroutineExceptionHandler? = null,
    job: Job = Job(),
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val scope = if (exceptionHandler != null)
        CoroutineScope(context + job + exceptionHandler)
    else
        CoroutineScope(context + job)
    return scope.launch(context, start, block)
}

fun <A, B> ofPair(first: A, second: B) = Pair(first, second)