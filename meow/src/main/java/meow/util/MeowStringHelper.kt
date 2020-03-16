@file:Suppress("unused")

package meow.util

import android.util.Base64
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by 1HE on 9/15/2018.
 */

@Suppress("PropertyName")
open class MeowStringHelper {

    val TEXT_MODE_DEFAULT = 0
    val TEXT_MODE_FIRST_ONLY = 1
    val TEXT_MODE_ALL = 2
    val TEXT_MODE_FIRST_SPACE = 3

    open fun generateUniqueId(): String {
        return UUID.randomUUID().toString().replace("-".toRegex(), "")
    }

    private fun decrypt(key: String, initVector: String, encrypted: String): String {
        return avoidExceptionReturn({
            val ivSpec = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec)

            val original = cipher.doFinal(Base64.decode(encrypted, 0))

            String(original)
        }) { "" }
    }
}

fun String?.trimCheckNull() = this?.trim() ?: ""

fun String?.trimAllSpaces() = this?.replace("\\s+".toRegex(), "") ?: ""

fun String?.isEmptyTrim(): Boolean {
    var s: String? = this ?: return true
    s = s.trimAllSpaces()
    return s == "" || s.isEmpty()
}

fun String?.isEmptyCheckNull(): Boolean {
    val s: String? = this ?: return true
    return s == "" || s!!.isEmpty()
}

fun String?.isEmptyWithNull(): Boolean {
    var s: String? = this ?: return true
    s = s.trimAllSpaces()
    return s == "" || s.isEmpty() || s.equals("null", ignoreCase = true)
}

fun String?.toPersianNumber(): String {
    val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    if (isEmptyCheckNull())
        return ""
    val out = StringBuilder()
    for (i in 0 until this!!.length) {
        when (val c = this[i]) {
            in '0'..'9' -> out.append(persianNumbers[Integer.parseInt(c.toString())])
            '٫' -> out.append('،')
            else -> out.append(c)
        }
    }
    return out.toString()
}

fun String?.toEnglishNumber(): String {
    val englishNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    if (isEmptyTrim())
        return ""
    val out = StringBuilder()
    for (i in 0 until this!!.length) {
        when (val c = this[i]) {
            in '۰'..'۹' -> out.append(englishNumbers[Integer.parseInt(c.toString())])
            '،' -> out.append('٫')
            else -> out.append(c)
        }
    }
    return out.toString()
}

fun String?.normalizePhoneNumber(): String {
    var s: String = this ?: return ""

    s = s.toEnglishNumber().trimAllSpaces()
    s = s.replace("[+]".toRegex(), "")

    if (!s.startsWith("98"))
        return s

    avoidException {
        s = "0" + s.substring(2)
    }

    return s
}

fun String?.normalizePhoneNumberI18N(): String {
    var s: String = this ?: return ""

    s = s.toEnglishNumber().trimAllSpaces()
    if (s.startsWith("09")) {
        avoidException {
            s = "+98" + s.substring(1)
        }
    }

    return s.trimAllSpaces()
}

fun String?.fetchAllDigit(): String {
    return if (isEmptyCheckNull())
        ""
    else
        toEnglishNumber().replace("[^\\d.]".toRegex(), "")
}

@Suppress("RegExpRedundantEscape")
fun String?.isValidEmail() =
    avoidExceptionReturn({
        Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE)
            .matcher(this).matches()
    }) { false }
