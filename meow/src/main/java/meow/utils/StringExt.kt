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

import java.util.*

/**
 * The Extensions of [String].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

fun generateUUID() = UUID.randomUUID().toString()

/**
 * Capitalize first character in string.
 * If input is empty return empty string.
 * @return a string that capitalized first character.
 */
fun String?.capitalizeFirst(): String {
    if (isNullOrEmpty())
        return ""

    val first = this!![0]
    return if (Character.isUpperCase(first)) {
        this
    } else {
        Character.toUpperCase(first) + this.substring(1)
    }
}

fun String?.isNotNullOrEmpty() = !isNullOrEmpty()