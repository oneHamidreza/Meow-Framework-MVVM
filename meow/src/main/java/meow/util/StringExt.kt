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

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import java.net.URL
import java.util.*

/**
 * Extensions of [String].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-20
 */

fun generateUUID() = UUID.randomUUID().toString()

fun String?.isNotNullOrEmpty() = !isNullOrEmpty()


/**
 * Trim a string with check nullability.
 * If input is null return empty string.
 * @return trimmed of string.
 */
fun String?.trimOrNull() = this?.trim() ?: ""

/**
 * Remove all spaces in a string with check nullability.
 * If input is null return empty string.
 * @return a string without any spaces.
 */
fun String?.trimAllSpaces() = this?.replace("\\s+".toRegex(), "") ?: ""

/**
 * Check a string that is empty with remove spaces.
 * If input is null return true.
 * @return a boolean describes a string is empty or not.
 */
fun String?.isEmptyTrimAllSpaces(): Boolean {
    var s: String? = this ?: return true
    s = s.trimAllSpaces()
    return s == "" || s.isEmpty()
}


/**
 * create a string from array.
 * If input is null return blocked string.
 * @return a string describes array.
 */
fun Array<String>?.createStringFromArray(): String {
    if (this == null)
        return "{}"
    return avoidException {
        if (size == 0)
            return "{}"

        val sb = StringBuilder()
        sb.append("{ ")
        for (i in indices) {
            sb.append(this[i])
            if (i != size - 1)
                sb.append(", ")
        }
        sb.append(" }")
        sb.toString()
    } ?: "{}"
}

/**
 * Normalize a url.
 * If input is empty return empty string.
 * @return a string describes normalized url.
 */
fun String?.normalizeUrl(): String {
    if (isEmptyTrimAllSpaces())
        return ""

    val pieces = this?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
    val textParts = ArrayList<String>()

    pieces?.let {
        for (piece in pieces) {
            avoidException(
                tryBlock = {
                    val isURL = URL(piece)
                    val protocol = isURL.protocol
                    val host = isURL.host
                    var query: String? = isURL.query
                    var path = isURL.path
                    var questionMark = "?"

                    if (path == "") {
                        path = "/"
                    }

                    if (query == null) {
                        query = ""
                        questionMark = ""
                    }

                    val url = "$protocol://$host$path$questionMark$query"
                    textParts.add(url)
                },
                exceptionBlock = {
                    textParts.add(piece)
                }
            )
        }
    }

    val resultString = StringBuilder()
    for (s in textParts) {
        resultString.append(s).append(" ")
    }

    return resultString.toString().trimOrNull()
}

/**
 * Capitalize first character in string.
 * If input is empty return empty string.
 * @return a string that capitalized first character.
 */
fun String?.capitalizeFirst(): String {
    if (isEmptyTrimAllSpaces())
        return ""

    val first = this?.get(0)
    return if (Character.isUpperCase(first!!)) {
        this!!
    } else {
        Character.toUpperCase(first) + this!!.substring(1)
    }
}

/**
 * Capitalize all characters in string.
 * If input is empty return empty string.
 * @return a string that capitalized all characters.
 */
fun String?.capitalizeAll(): String {
    if (isEmptyTrimAllSpaces())
        return this ?: ""

    val ch = this!!.toCharArray()
    for (i in ch.indices)
        ch[i] = Character.toUpperCase(ch[i])
    return String(ch)
}

/**
 * Capitalize per word in string.
 * If input is empty return empty string.
 * @return a string that capitalized per word.
 */
fun String?.capitalizeFirstSpace(): String {
    if (isEmptyTrimAllSpaces())
        return this ?: ""

    val ch = this!!.toCharArray()
    ch[0] = Character.toUpperCase(ch[0])
    for (i in 1 until ch.size) {
        if (i != ch.size - 1) {
            if (ch[i - 1] == ' ')
                ch[i] = Character.toUpperCase(ch[i])
        }
    }
    return String(ch)
}


/**
 * Convert numbers in string to persian format.
 * If input is empty return false.
 * @return a string with persian numbers.
 */
@Suppress("UnnecessaryVariable")
fun String?.toPersianNumber(): String {
    val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    if (isNullOrEmpty())
        return ""
    val out = StringBuilder()
    for (element in this!!) {
        when (val c = element) {
            in '0'..'9' -> out.append(persianNumbers[Integer.parseInt(c.toString())])
            '٫' -> out.append('،')
            else -> out.append(c)
        }
    }
    return out.toString()
}

/**
 * Convert numbers in Editable to persian format.
 * If input is empty return false.
 * @return a Editable with persian numbers.
 */
@Suppress("UnnecessaryVariable")
fun Editable?.toPersianNumber(): Editable {
    val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    if (isNullOrEmpty())
        return SpannableStringBuilder("")
    val out = StringBuilder()
    for (element in this!!) {
        when (val c = element) {
            in '0'..'9' -> out.append(persianNumbers[Integer.parseInt(c.toString())])
            '٫' -> out.append('،')
            else -> out.append(c)
        }
    }
    return SpannableStringBuilder(out)
}

/**
 * Convert numbers in string to english format.
 * If input is empty return false.
 * @return a string with english numbers.
 */
@Suppress("UnnecessaryVariable")
fun String?.toEnglishNumber(): String {
    val englishNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    if (isNullOrEmpty())
        return ""
    val out = StringBuilder()
    for (element in this!!) {
        when (val c = element) {
            in '۰'..'۹' -> out.append(englishNumbers[Integer.parseInt(c.toString())])
            '،' -> out.append('٫')
            else -> out.append(c)
        }
    }
    return out.toString()
}

/**
 * Convert numbers in Editable to english format.
 * If input is empty return false.
 * @return a Editable with english numbers.
 */
@Suppress("UnnecessaryVariable")
fun Editable?.toEnglishNumber(): Editable {
    val englishNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    if (isNullOrEmpty())
        return SpannableStringBuilder("")
    val out = StringBuilder()
    for (element in this!!) {
        when (val c = element) {
            in '۰'..'۹' -> out.append(englishNumbers[Integer.parseInt(c.toString())])
            '،' -> out.append('٫')
            else -> out.append(c)
        }
    }
    return SpannableStringBuilder(out)
}

/**
 * Get string array from arrays resourse.
 * If input is empty return false.
 * @return a array of strings.
 */
fun Context.getStringArray(res: Int): Array<String> = resources.getStringArray(res)

/**
 * Fetch all digits in a string.
 * If input is empty return empty string.
 * @return a string that contains only digits.
 */
fun String?.fetchAllDigit() =
    if (isNullOrEmpty())
        ""
    else
        toEnglishNumber().replace("[^\\d.]".toRegex(), "")

/**
 * Convert a string to regex pattern.
 * If input is empty return empty string.
 * @param original is a string that
 * @return a string that contains only digits.
 */
fun String?.toPatternRegex(original: String, rx: String): String {
    if (this == null)
        return ""
    return replace(Regex(original), rx)
}

/**
 * Normalize a phone number to iran local format.
 * If input is empty return empty string.
 * @return normalized phone number for iran local.
 */
fun String?.normalizePhoneNumber(): String {
    var s: String = this ?: return ""

    s = s.toEnglishNumber().trimAllSpaces()
    s = s.replace("[+]".toRegex(), "")
        .replace("[-]".toRegex(), "")
        .replace("[(]".toRegex(), "")
        .replace("[)]".toRegex(), "")

    if (!s.startsWith("98"))
        return s

    avoidException {
        s = "0" + s.substring(2)
    }

    return s
}

/**
 * Normalize a iran local phone number to i18n format.
 * If input is empty return empty string.
 * @return normalized phone number for i18n format.
 */
fun String?.normalizePhoneNumberI18N(): String {
    var s: String = this ?: return ""

    s = s.toEnglishNumber().trimAllSpaces()
    s = s.replace("[+]".toRegex(), "")
        .replace("[-]".toRegex(), "")
        .replace("[(]".toRegex(), "")
        .replace("[)]".toRegex(), "")
    if (s.startsWith("98")) {
        s = "+$s"
    }
    if (s.startsWith("09")) {
        avoidException {
            s = "+98" + s.substring(1)
        }
    }

    return s.trimAllSpaces()
}

/**
 * Convert a string to integer if it is possible.
 * If input is empty return zero value.
 * @return a integer describes value in a string.
 */
fun String?.convertToInt() = avoidException {
    if (this == null || isEmptyTrimAllSpaces())
        return 0

    toInt()
} ?: 0

/**
 * Create a spannable object with specified color per words.
 * @return a spannable that contains a color per given words.
 */
fun createSpan(text: String, argb: Int, vararg words: String): Spannable {
    val spannable = SpannableString(text)
    var substringStart = 0
    var start: Int
    words.forEach {
        while ((text.indexOf(it, substringStart)) >= 0) {
            start = text.indexOf(it, substringStart)
            spannable.setSpan(
                ForegroundColorSpan(argb), start, start + it.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            substringStart = start + it.length
        }
    }
    return spannable
}

/**
 * Remove extra spaces in a string.
 * If input is empty return empty string.
 * @return a string without extra spaces.
 */
fun String?.removeExtraSpaces(): String {
    if (isNullOrEmpty())
        return ""
    return this!!.trim().replace(" +".toRegex(), " ")
}

/**
 * Add http to first of url if it has been not exist.
 * If input is empty return empty string.
 * @return a string that start with http.
 */
fun String?.addHttpIfNeed(): String {
    if (this == null)
        return ""
    if (this.startsWith("http", false) || this.startsWith("https", false))
        return this

    return "http://$this"
}