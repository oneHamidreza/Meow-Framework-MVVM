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

import android.webkit.URLUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.regex.Pattern

/**
 * [String] Validate Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-20
 */


/**
 * Check validation of URL.
 * If input is empty return false.
 * @return a boolean describes validation of URL.
 */
fun String?.isValidUrl() = avoidException {
    URLUtil.isValidUrl(this)
} ?: false

/**
 * Check validation of Email.
 * If input is empty return false.
 * @return a boolean describes validation of Email.
 */
@Suppress("RegExpRedundantEscape")
fun String?.isValidEmail() = avoidException {
    if (isNullOrEmpty())
        return false
    Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE)
        .matcher(this!!).matches()
} ?: false

/**
 * Check validation of Password.
 * If input is empty return false.
 * @param minimum is a integer that describes minimum character count in a string.
 * @return a boolean describes validation of Password.
 */
fun String?.isValidPassword(minimum: Int = 6) = avoidException {
    !isEmptyTrimAllSpaces() && this?.length ?: 0 >= minimum
} ?: false

/**
 * Check validation of Website.
 * If input is empty return false.
 * @return a boolean describes validation of website.
 */
fun String?.isValidWebsite() = avoidException {
    if (isNullOrEmpty())
        return false
    Pattern.compile(
        "\\b(?:(https?|ftp|file)://|www\\.)?[-A-Z0-9+&#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]\\.[-A-Z0-9+&@#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]",
        Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE
    ).matcher(this!!).matches()
} ?: false

/**
 * Check validation of Username.
 * If input is empty return false.
 * @return a boolean describes validation of Username.
 */
fun String?.isValidUsername() = avoidException {
    if (isNullOrEmpty())
        return false
    Pattern.compile("[a-z][a-z0-9_]{3,}", Pattern.CASE_INSENSITIVE).matcher(this!!).matches()
} ?: false

/**
 * Check validation of Color.
 * If input is empty return false.
 * @return a boolean describes validation of Color.
 */
fun String?.isValidColor() = avoidException {
    if (isNullOrEmpty())
        return false
    val pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    pattern.matcher(this!!).matches()
} ?: false

/**
 * Check validation of Legacy Mobile.
 * If input is empty return false.
 * @return a boolean describes validation of Legacy Mobile.
 */
fun String?.isValidMobileLegacy() = avoidException {
    if (isNullOrEmpty())
        return false
    val s = toEnglishNumber()
    val chars = s.toCharArray()
    for (c in chars) {
        if (!Character.isDigit(c)) {
            return false
        }
    }
    s.length == 11 && s.startsWith("09")
} ?: false

/**
 * Check validation of Mobile.
 * If input is empty return false.
 * @return a boolean describes validation of Mobile.
 */
fun String.isValidMobile(): Boolean {

    fun getCountryISOFromPhone(s: String?): String {
        val mPhoneNumberUtil = PhoneNumberUtil.getInstance()
        var region = ""
        if (s != null) {
            avoidException {
                var phone = s
                if (!phone.contains("+"))
                    phone = "+$phone"
                val p = mPhoneNumberUtil.parse(phone, null)
                val sb = StringBuilder(16)
                sb.append('+').append(p.countryCode).append(p.nationalNumber)
                region = mPhoneNumberUtil.getRegionCodeForNumber(p)
            }
        }
        return region
    }

    val countryIso = getCountryISOFromPhone(this)
    val util = PhoneNumberUtil.getInstance()
    return avoidException {
        if (this.startsWith("+98") || this.startsWith("98")) {
            val m = this.fetchAllDigit()
            return m.isNotEmpty() && m.startsWith("98") && m.length == 12
        }
        util.isValidNumber(util.parse(this, countryIso))
    } ?: false
}

/**
 * Check validation of Identity Code Iran Country.
 * If input is empty return false.
 * @return a boolean describes validation of Identity Code.
 */
fun String?.isValidIdentityCodeIran() = avoidException {
    if (isNullOrEmpty())
        return false

    if (this == "0000000000")
        return false

    val a = IntArray(11)
    var sum = 0
    val m: Int
    val b: Int
    a[0] = 0
    val arr = this!!.toCharArray()

    for (i in arr.indices) {
        a[10 - i] = arr[i].toInt() - 48
        if (i != 9)
            sum += a[10 - i] * (10 - i)
    }

    m = a[1]
    b = sum % 11

    b == m || 11 - b == m
} ?: false