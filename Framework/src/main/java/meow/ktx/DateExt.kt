@file:Suppress("unused")

package meow.ktx

import android.content.Context
import com.etebarian.meowframework.R
import meow.MeowController
import meow.controller
import java.util.*

/**
 * Date Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-20
 */

//todo improve by native classes

fun Context.getStringArrayCalendar(res: Int): Array<String> {
    val calendarMode = controller.calendar
    val resArray = when (res) {
        R.array.georgian_week -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.array.jalali_week
        }
        R.array.georgian_month -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.array.jalali_month
        }
        else -> 0
    }
    return getStringArray(resArray)
}

fun Context.getStringCalendar(res: Int): String {
    val calendarMode = controller.calendar
    val resString = when (res) {
        R.string.georgian_today -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_today
        }
        R.string.georgian_moreDays -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_moreDays
        }
        R.string.georgian_yesterday -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_yesterday
        }
        R.string.georgian_amDate -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_amDate
        }
        R.string.georgian_pmDate -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_pmDate
        }
        R.string.georgian_ago -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_ago
        }
        R.string.georgian_nowTime -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_nowTime
        }
        R.string.georgian_minute -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_minute
        }
        R.string.georgian_hour -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_hour
        }
        R.string.georgian_day -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_day
        }
        R.string.georgian_week -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_week
        }
        R.string.georgian_month -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_month
        }
        R.string.georgian_year -> {
            if (calendarMode == MeowController.Calendar.GEORGIAN) res
            else R.string.jalali_year
        }
        else -> 0
    }
    return getString(resString)
}

fun Int.toTwoDigit(): String {
    return if (this < 10) "0$this" else toString()
}

fun Context?.getTimeTwelveHour(calendar: Calendar): String {
    if (this == null)
        return ""
    avoidException {
        val amText = getStringCalendar(R.string.georgian_amDate)
        val pmText = getStringCalendar(R.string.georgian_pmDate)
        var hour = calendar.get(Calendar.HOUR)
        val amPm = calendar.get(Calendar.AM_PM)
        val minute = calendar.get(Calendar.MINUTE).toTwoDigit()
        return if (amPm == Calendar.AM) {
            "$hour:$minute $amText"
        } else {
            if (hour == 0)
                hour = 12
            "$hour:$minute $pmText"
        }
    }
    return ""
}

fun Context?.dateFormatSimple(calendar: Calendar) =
    this?.dateFormatSimple(calendar.timeInMillis)//ex: 2 years ago

fun Context?.dateFormatSimple(timestamp: Long): String {//ex: 2 years ago
    if (this == null)
        return ""
    if (timestamp <= 0)
        return ""

    val minuteText = getStringCalendar(R.string.georgian_minute)
    val hourText = getStringCalendar(R.string.georgian_hour)
    val dayText = getStringCalendar(R.string.georgian_day)
    val weekText = getStringCalendar(R.string.georgian_week)
    val monthText = getStringCalendar(R.string.georgian_month)
    val yearText = getStringCalendar(R.string.georgian_year)
    val agoText = getStringCalendar(R.string.georgian_ago)
    val nowTime = getStringCalendar(R.string.georgian_nowTime)

    avoidException {
        val time = Calendar.getInstance()
        time.timeInMillis = timestamp

        val now = JalaliCalendar()
        val nowT = now.timeInMillis
        var d = nowT - timestamp
        d /= 1000

        if (d < 60)
            return nowTime

        if (d < 60 * 60) {
            val s = d.toInt() / 60
            return "$s $minuteText $agoText"
        }

        if (d < 24 * 3600) {
            val s = d.toInt() / 3600
            return "$s $hourText $agoText"
        }

        if (d < 7 * 24 * 3600) {
            val s = d.toInt() / (24 * 3600)
            return "$s $dayText $agoText"
        }

        if (d < 30 * 24 * 3600) {
            val s = (d / (24 * 3600)).toInt() / 7 + 1
            return "$s $weekText $agoText"
        }

        if (d < 12 * 30 * 24 * 3600) {
            val s = d.toInt() / (30 * 24 * 3600)
            return "$s $monthText $agoText"
        }

        val s = d.toInt() / (12 * 30 * 24 * 3600)
        return "$s $yearText $agoText"
    }

    return ""
}

fun Context?.dateFormatDetail(calendar: Calendar) =
    dateFormatDetail(calendar.timeInMillis)//ex: yesterday 20:50 a.m.

fun Context?.dateFormatDetail(timestamp: Long): String {//ex: yesterday 20:50 a.m.
    if (this == null)
        return ""
    if (timestamp <= 0)
        return ""

    val todayText = getStringCalendar(R.string.georgian_today)
    val yesterdayText = getStringCalendar(R.string.georgian_yesterday)
    val weekTitles = getStringArrayCalendar(R.array.georgian_week)
    val monthTitles = getStringArrayCalendar(R.array.georgian_month)

    return avoidException {
        val time = Calendar.getInstance()
        time.timeInMillis = timestamp

        val t: Calendar
        if (controller.calendar == MeowController.Calendar.GEORGIAN) {
            t = Calendar.getInstance()
            t.timeInMillis = timestamp
        } else {
            t = JalaliCalendar(timestamp)
        }
        val now: Calendar = if (controller.calendar == MeowController.Calendar.GEORGIAN) {
            Calendar.getInstance()
        } else {
            JalaliCalendar()
        }

        val yearNow = now.get(Calendar.YEAR)
        val dayOfYearNow = now.get(Calendar.DAY_OF_YEAR)
        val weekOfYearNow = now.get(Calendar.WEEK_OF_YEAR)
        val yearT = t.get(Calendar.YEAR)
        val dayOfYearT = t.get(Calendar.DAY_OF_YEAR)
        val dayOfMonthT = t.get(Calendar.DAY_OF_MONTH)
        val monthT = t.get(Calendar.MONTH)
        val weekOfYearT = t.get(Calendar.WEEK_OF_YEAR)
        val dayOfWeekT = t.get(Calendar.DAY_OF_WEEK)

        if (yearNow != yearT) {
            return if (controller.calendar == MeowController.Calendar.GEORGIAN)
                dayOfMonthT.toTwoDigit() + "/" + (monthT + 1).toTwoDigit() + "/" + yearT
            else
                dayOfMonthT.toString() + " " + monthTitles[monthT] + " " + yearT
        }

        if (dayOfYearNow == dayOfYearT) {
            return todayText + " " + getTimeTwelveHour(time)
        }

        if (dayOfYearNow - 1 == dayOfYearT) {
            return yesterdayText + " " + getTimeTwelveHour(time)
        }

        return if (weekOfYearNow == weekOfYearT) {
            weekTitles[dayOfWeekT - 1] + " " + getTimeTwelveHour(time)
        } else {
            if (controller.calendar == MeowController.Calendar.GEORGIAN)
                monthTitles[monthT] + " " + dayOfMonthT.toString()
            else
                dayOfMonthT.toString() + " " + monthTitles[monthT]
        }

    } ?: ""
}

fun Long.createJalaliCalendar(): JalaliCalendar.YearMonthDate {
    val c = Calendar.getInstance()
    c.timeInMillis = this
    return JalaliCalendar.gregorianToJalali(
        JalaliCalendar.YearMonthDate(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
    )
}

fun String?.fetchTimestampWithClock() = avoidException {
    if (this == null)
        return 0L

    val all = split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    if (all.size < 2)
        return 0L

    val date = all[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val time = all[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val c = Calendar.getInstance()
    c.timeInMillis = 0
    c.set(
        Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]),
        Integer.valueOf(time[0]), Integer.valueOf(time[1]), Integer.valueOf(time[2])
    )
    c.timeInMillis
} ?: 0L

fun String?.fetchTimestamp() = avoidException {
    if (this == null || contains("-"))
        return 0L

    val year = substring(0, 4).toInt()
    val month = substring(4, 6).toInt()
    val date = substring(6, 8).toInt()
    val c = Calendar.getInstance()
    c.set(year, month - 1, date, 0, 0, 0)
    c.timeInMillis
} ?: 0L

fun String?.fetchTimestampWithDash() = avoidException {
    if (this == null)
        return 0L

    val d = split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val year = d[0].toInt()
    val month = d[1].toInt()
    val date = d[2].toInt()
    val c = Calendar.getInstance()
    c.set(year, month - 1, date, 0, 0, 0)
    c.timeInMillis
} ?: 0L

fun Context?.dateFormatNormal(calendar: Calendar) =
    dateFormatNormal(calendar.timeInMillis)//ex: 23 month 1397

fun Context?.dateFormatNormal(time: Long): String {//ex: 23 month 1397
    if (this == null)
        return ""
    return avoidException {
        val months = getStringArrayCalendar(R.array.georgian_month)
        if (controller.calendar == MeowController.Calendar.GEORGIAN) {
            val c = Calendar.getInstance()
            c.timeInMillis = time
            c.get(Calendar.DAY_OF_MONTH)
                .toString() + " " + months[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR)
        } else {
            val jalali = time.createJalaliCalendar()
            jalali.date.toString() + " " + months[jalali.month] + " " + jalali.year
        }
    } ?: ""
}

fun Long.dateSmall() = avoidException {//ex: 6/20
    if (controller.calendar == MeowController.Calendar.GEORGIAN) {
        val c = Calendar.getInstance()
        c.timeInMillis = this
        (c.get(Calendar.MONTH) + 1).toString() + "/" + c.get(Calendar.DAY_OF_MONTH)
    } else {
        val jalali = createJalaliCalendar()
        (jalali.month + 1).toString() + "/" + jalali.date
    }
} ?: ""

fun Context?.dateFormatNormalWithTime(time: Long): String {//ex: 23 mehr 1397 14:15
    if (this == null)
        return ""
    return avoidException {
        val months = getStringArrayCalendar(R.array.georgian_month)
        if (controller.calendar == MeowController.Calendar.GEORGIAN) {
            val c = Calendar.getInstance()
            c.timeInMillis = time
            c.get(Calendar.DAY_OF_MONTH)
                .toString() + " " + months[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR) +
                    " " + getTimeTwelveHour(c)
        } else {
            val jalali = time.createJalaliCalendar()
            val c = Calendar.getInstance()
            c.timeInMillis = time
            jalali.date.toString() + " " + months[jalali.month] + " " + jalali.year +
                    " " + getTimeTwelveHour(c)
        }
    } ?: ""
}

fun Calendar.canUseApp(minimumAge: Int): Boolean {
    val now = Calendar.getInstance()
    now.add(Calendar.YEAR, -minimumAge)
    return !now.before(this)
}

fun String.createCalendarByStringDateOnly() = avoidException {
    if (isEmpty())
        return Calendar.getInstance()

    if (controller.calendar == MeowController.Calendar.GEORGIAN) {
        val date = split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val c = Calendar.getInstance()
        c.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]))
        c
    } else {
        val date = split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val jalaliCalendar = JalaliCalendar()
        jalaliCalendar.set(
            Integer.valueOf(date[2]),
            Integer.valueOf(date[1]) - 1,
            Integer.valueOf(date[0])
        )
        val c = Calendar.getInstance()
        c.timeInMillis = jalaliCalendar.timeInMillis
        c
    }
} ?: Calendar.getInstance()

private fun Int.isLeapYear(): Boolean {
    return if (this % 4 != 0) {
        false
    } else
        this % 400 == 0 || this % 100 != 0
}