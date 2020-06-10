@file:Suppress("unused")

package meow.ktx

import android.content.Context
import androidx.fragment.app.Fragment
import com.etebarian.meowframework.R
import meow.MeowController
import meow.controller
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/**
 * Currency Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-01
 */

fun Double?.formatCurrencyRial(dec: Int = 0, original: String? = null) = avoidException {
    val sb = StringBuilder("#.")
    for (i in 0 until dec) {
        sb.append("#")
    }
    val otherSymbol = DecimalFormatSymbols(Locale.getDefault())
    otherSymbol.decimalSeparator = '.'
    otherSymbol.groupingSeparator = '.'
    val nf = DecimalFormat(sb.toString(), otherSymbol)
    var s = nf.format(this)
    val forceDotCheck = original != null && original[original.length - 1] == '.'
    if (forceDotCheck) {
        s = original!!.replace("[,]".toRegex(), "")
    }
    s = s.replace("\\s+".toRegex(), "")
    s = s.replace("[+-]".toRegex(), "")
    val out = StringBuilder()
    var arr: Array<String>? = null
    if (s.contains(".")) {
        arr = s.split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        s = arr[0]
    }
    val ch = s.toCharArray()
    for (i in ch.indices) {
        if ((ch.size - i) % 3 == 0 && i != 0) {
            out.append(",")
        }
        out.append(ch[i])
    }
    if (!forceDotCheck) {
        if (arr != null && arr.size == 2) {
            out.append(".").append(arr[1])
        }
    } else {
        if (original!!.contains(".") && arr != null && arr.size == 1)
            out.append(".")
    }
    out.toString()
} ?: controller.currency.default

fun Double?.formatCurrencyUSD() = avoidException {
    BigDecimal(this ?: 0.0).setScale(2, RoundingMode.HALF_UP).toString().formatCurrencyUSD()
} ?: controller.currency.default

fun String?.formatCurrencyUSD() = avoidException {
    var s = this
    if (isNullOrEmpty())
        controller.currency.default

    val splits = s!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    if (!s.contains(".")) {
        s += ".00"
    } else if (splits.size == 2) {
        val az = splits[1]
        if (az.length == 1)
            s += "0"
    } else if (splits.size == 1) {
        s += "00"
    }
    s = BigDecimal(s.replace("[$,]".toRegex(), "")).setScale(2, RoundingMode.HALF_UP).toString()

    val cleanString = s.replace("[$,.()]".toRegex(), "")
    val parsed = BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_HALF_EVEN)
        .divide(BigDecimal(100), BigDecimal.ROUND_HALF_EVEN)
    NumberFormat.getCurrencyInstance(Locale.US).format(parsed).replace("[$]".toRegex(), "")
} ?: controller.currency.default

fun String?.toDoubleCurrency() = avoidException {
    if (isNullOrEmpty())
        return 0.0
    this?.toEnglishNumber()?.replace("[^\\d.]".toRegex(), "")?.toDouble() ?: 0.0
} ?: 0.0

fun String?.formatCurrency(dec: Int = 0) = avoidException {
    if (isNullOrEmpty())
        controller.currency.default

    when (controller.currency) {
        MeowController.Currency.USD -> this.formatCurrencyUSD()
        MeowController.Currency.RIAL, MeowController.Currency.TOMAN -> this.toDoubleCurrency()
            .formatCurrencyRial(dec, this)
    }
} ?: controller.currency.default

fun Double?.formatCurrency(dec: Int = 0) = avoidException {
    if (this == null)
        controller.currency.default

    when (controller.currency) {
        MeowController.Currency.USD -> this.formatCurrencyUSD()
        MeowController.Currency.RIAL, MeowController.Currency.TOMAN -> this.formatCurrencyRial(
            dec,
            BigDecimal(this!!).toString()
        )
    }
} ?: controller.currency.default

fun String?.formatCurrencyEmpty(dec: Int = 0) = avoidException {
    if (isNullOrEmpty())
        return ""

    this.formatCurrency(dec)
} ?: ""

fun Context?.createCurrency(s: String?, dec: Int = 0): String {
    if (this == null)
        return s ?: ""
    return when (controller.currency) {
        MeowController.Currency.USD -> "$" + s.formatCurrency(dec)
        MeowController.Currency.RIAL -> s.formatCurrency(dec) + " " + getString(R.string.currency_rial)
        MeowController.Currency.TOMAN -> s.formatCurrency(dec) + " " + getString(R.string.currency_toman)
    }
}

fun Fragment?.createCurrency(s: String?, dec: Int = 0) =
    this?.requireContext().createCurrency(s, dec)

fun Context?.createCurrency(d: Double?, dec: Int = 0): String {
    if (this == null)
        return ""
    return createCurrency(BigDecimal(d ?: 0.0).toString(), dec)
}

fun Fragment?.createCurrency(d: Double?, dec: Int = 0) =
    this?.requireContext().createCurrency(d, dec)

fun BigDecimal.createPrice(): BigDecimal = setScale(2, BigDecimal.ROUND_HALF_EVEN)

fun String?.numToWord(): String {
    if (this == null)
        return ""
    return avoidException {
        toDoubleCurrency().numToWord().removeExtraSpaces()
    } ?: ""
}

fun Double?.numToWord(): String {
    if (this == null)
        return ""
    return avoidException {
        return convert(BigDecimal(this).toDouble()).removeExtraSpaces()
    } ?: ""
}

const val strAnd = " و "
val units = arrayOf(
    "",
    "یک",
    "دو",
    "سه",
    "چهار",
    "پنج",
    "شش",
    "هفت",
    "هشت",
    "نه",
    "ده",
    "یازده",
    "دوازده",
    "سیزده",
    "چهارده",
    "پانزده",
    "شانزده",
    "هفده",
    "هجده",
    "نوزده"
)
val dahgan = arrayOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")
val sadghan =
    arrayOf("", "یکصد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")
val steps = arrayOf(
    "هزار",
    "میلیون",
    "میلیارد",
    "تریلیون",
    "کادریلیون",
    "کوینتریلیون",
    "سکستریلیون",
    "سپتریلیون",
    "اکتریلیون",
    "نونیلیون",
    "دسیلیون"
)

internal fun convert(i: Double): String {
    if (i < 20) return units[i.toInt()]
    if (i < 100) return dahgan[i.toInt() / 10] + if (i % 10 >= 1) strAnd + convert(i % 10) else ""
    if (i < 1000)
        return sadghan[i.toInt() / 100] + if (i % 100 >= 1) strAnd + convert(i % 100) else ""
    if (i < 1000000)
        return convert(i / 1000) + stepsString(steps[0]) + if (i % 1000 > 0) strAnd + convert(i % 1000) else ""
    return if (i < 1000000000) convert(i / 1000000) + stepsString(steps[1]) + (if (i % 1000000 > 0) strAnd + convert(
        i % 1000000
    ) else "") else convert(i / 1000000000) + stepsString(steps[2]) + if (i % 1000000000 > 0) strAnd + convert(
        i % 1000000000
    ) else ""
}

internal fun stepsString(step: String): String {
    return " $step "
}
