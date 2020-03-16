@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package meow.util

import android.util.Log
import meow.controller

/**
 * Created by 1HE on 9/16/2018.
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

fun logV(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.v(t, m.toString(), tr)
    } else 0
}

fun logI(tag: String = "", m: Any?, tr: Throwable? = null): Int {
    return if (controller.isDebugMode) {
        val t = createTag(tag)
        printSystemIfTest(t, m.toString(), tr)
        Log.i(t, m.toString(), tr)
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

    val t = avoidException(
        tryBlock = {
            val stackThread = Thread.currentThread().stackTrace
            stackThread[5].fileName.substringBefore(".")
        },
        exceptionBlock = { "UNKNOWN" }
    )

    val s = StringBuilder()
    s.append(t)
    if (tag.isNotEmpty()) {
        s.append("_")
        s.append(tag)
    }
    s.append("_meowL")

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
