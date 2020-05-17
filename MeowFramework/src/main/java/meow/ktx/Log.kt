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

package meow.ktx

import android.util.Log
import meow.controller

/**
 * Log Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

object MeowLog {

    var isFromJUnit = false

}

fun logD(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.d(t, m.toString(), tr)
    } else 0
}

fun logE(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.e(t, m.toString(), tr)
    } else 0
}

fun logI(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.i(t, m.toString(), tr)
    } else 0
}

fun logV(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.v(t, m.toString(), tr)
    } else 0
}

fun logW(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.w(t, m.toString(), tr)
    } else 0
}

private fun createTag(tag: String): String {
    if (controller.isLogTagNative)
        return if (tag.isEmpty()) "meow" else tag

    val t = avoidException {
        val stackThread = Thread.currentThread().stackTrace
        stackThread[5].fileName.substringBefore(".")
    } ?: "UNKNOWN"

    val s = StringBuilder()
    s.append(t)
    if (tag.isNotEmpty()) {
        s.append("_")
        s.append(tag)
    }

    if (s.length > 32)
        return s.toString().removeRange(0..(s.length - 32))

    return s.toString()
}

@Suppress("ConstantConditionIf")
private fun printSystemIfTest(tag: String, message: String, tr: Throwable? = null) {
    if (MeowLog.isFromJUnit) {
        if (tr == null)
            println("$tag : $message")
        else
            println("$tag : $message , tr : $tr")
    }
}
