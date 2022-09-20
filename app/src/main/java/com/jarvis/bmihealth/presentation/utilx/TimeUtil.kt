@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER", "NAME_SHADOWING")

package com.jarvis.bmihealth.presentation.utilx

import android.annotation.SuppressLint
import android.content.Context
import com.jarvis.bmihealth.R
import com.jarvis.locale_helper.helper.LocaleHelper
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Suppress("unused","ConstantLocale")
object TimeUtil {
    val dM = SimpleDateFormat("d/M", Locale.getDefault())
    val ddMMyyyy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val hhmma = SimpleDateFormat("hh:mm aaa", Locale.getDefault())
    val HHmm = SimpleDateFormat("HH:mm", Locale.getDefault())
    val hhmmss = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    val mmss = SimpleDateFormat("mm:ss", Locale.getDefault())

    const val EEEddMMyyyy = "EEE, dd MMMM yyyy"
    const val MMMdd = "MMM dd"
    const val ddMMM = "dd MMM"
    const val simpleDateFormatMonthDateYear = "MMMM, yyyy"
    const val simpleMMMddyyyy = "MMM dd, yyyy"
    const val MMMMyyyy = "MMMM yyyy"
    const val yyyyMMdd = "yyyyMMdd"
    val MMMddyyyy = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    fun capitalizeDate(dateString: String): String {
        val dateStringBuilder = StringBuilder(dateString.lowercase(Locale.getDefault()))
        for (i in dateStringBuilder.indices) {
            val c = dateStringBuilder[i]
            if (c in 'a'..'z' || c in 'A'..'Z') {
                dateStringBuilder.setCharAt(i, Character.toUpperCase(dateStringBuilder[i]))
                break
            }
        }
        return dateStringBuilder.toString()
    }

    fun formatDateDay(dateInMillis: Long): String {
        val date = Date(dateInMillis)
        return ddMMyyyy.format(date)
    }

    fun formatDateMonthDayYear(dateInMillis: Long): String {
        val date = Date(dateInMillis)
        return MMMddyyyy.format(date)
    }


    @SuppressLint("SimpleDateFormat")
    fun formatDateDayHHmmss(dateInMillis: Long): String {
        val date = Date(dateInMillis)
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        dateFormat.format(dateInMillis)
        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatMonthMMM(dateInMillis: Long): String {
        val date = Date(dateInMillis)
        val dateFormat = SimpleDateFormat("MMM")
        dateFormat.format(dateInMillis)
        return dateFormat.format(date)
    }

    fun formatDateWithFormat(dateInMillis: Long, pattern: String, context: Context): String {
        val date = Date(dateInMillis)
        val simpleDateFormat =
            SimpleDateFormat(pattern, Locale.getDefault())
        return capitalizeDate(simpleDateFormat.format(date))
    }

    fun formatDateWithPattern(dateInMillis: Long, locale: Locale?, pattern: String?): String {
        val date = Date(dateInMillis)
        val sdf = SimpleDateFormat(pattern, locale)
        return capitalizeDate(sdf.format(date))
    }


    fun formatDateWithFormat(dateInMillis: Long, simpleDateFormat: SimpleDateFormat): String {
        val date = Date(dateInMillis)
        return simpleDateFormat.format(date)
    }

    fun formatDate(dateInMillis: Long, context: Context): String {
        val date = Date(dateInMillis)
        val sdf: SimpleDateFormat = if (LocaleHelper.getInstance().getCurrentLanguageCode(context)
                .equals("vi", ignoreCase = true)
        ) {
            SimpleDateFormat("dd MMM, HH:mm", LocaleHelper.getInstance().getLocale(context))
        } else {
            SimpleDateFormat("MMM dd, HH:mm", LocaleHelper.getInstance().getLocale(context))
        }
        return capitalizeDate(sdf.format(date))
    }

    fun formatDateYYYY(dateInMillis: Long, context: Context): String {
        val date = Date(dateInMillis)
        val sdf: SimpleDateFormat = if (LocaleHelper.getInstance().getCurrentLanguageCode(context)
                .equals("vi", ignoreCase = true)
        ) {
            SimpleDateFormat("dd MMM, yyyy", LocaleHelper.getInstance().getLocale(context))
        } else {
            SimpleDateFormat("MMM dd, yyyy", LocaleHelper.getInstance().getLocale(context))
        }
        return capitalizeDate(sdf.format(date))
    }

    fun getEndOfDay(calendar: Calendar): Long {
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DATE]
        calendar[year, month, day, 23, 59] = 59
        return calendar.timeInMillis
    }

    fun convertShortDate(timeSelect: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeSelect
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        val day = calendar[Calendar.DAY_OF_MONTH]
        return year.toString() + String.format("%02d", month) + String.format("%02d", day)
    }

    fun convertShortTime(timeSelect: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeSelect
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        val day = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        return year.toString() + String.format("%02d", month) + String.format(
            "%02d",
            day
        ) + String.format("%02d", hour) + String.format("%02d", minute)
    }

    fun checkLeftTime(timeCheckShowDialog: Long): Boolean {
        return System.currentTimeMillis() - timeCheckShowDialog < 900
    }

    fun checkLeftTime(timeCheckShowDialog: Long, range: Int): Boolean {
        return System.currentTimeMillis() - timeCheckShowDialog < range
    }

    fun getDifferenceDays(startTime: Long, endTime: Long): Long {
        val startTimeStr: String = formatDateDay(startTime)
        val endTimeStr: String = formatDateDay(endTime)
        val date1: Date = ddMMyyyy.parse(startTimeStr) as Date
        val date2: Date = ddMMyyyy.parse(endTimeStr) as Date
        val diff = abs(date1.time - date2.time)
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1
    }

    fun getDifferenceHours(startTime: Long, endTime: Long): Long {
        val startTimeStr: String = formatDateDay(startTime)
        val endTimeStr: String = formatDateDay(endTime)
        val date1: Date = ddMMyyyy.parse(startTimeStr) as Date
        val date2: Date = ddMMyyyy.parse(endTimeStr) as Date
        val diff = abs(date1.time - date2.time)
        return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) + 1
    }

    fun getDifferenceMinutes(startTime: Long, endTime: Long): Int {
        return abs((endTime - startTime) / (1000 * 60)).toInt()
    }

    fun monthsBetween(a: Date, b: Date?): Int {
        var b = b
        val cal = Calendar.getInstance()
        if (a.before(b)) {
            cal.time = a
        } else {
            if (b != null) {
                cal.time = b
            }
            b = a
        }
        var c = 0
        while (cal.time.before(b)) {
            cal.add(Calendar.MONTH, 1)
            c++
        }
        return c - 1
    }


    fun getMonthsBetween(startTimeInMilliSeconds: Long?, endTimeInMilliSeconds: Long?): Int {
        val a = Date(startTimeInMilliSeconds!!)
        val b = Date(endTimeInMilliSeconds!!)
        return monthsBetween(a, b)
    }


    fun daysBetween(startTime: Long, endTime: Long): Long {
        val startDateValue = Date(startTime)
        val endDateValue = Date(endTime)
        val diff: Long = endDateValue.time - startDateValue.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return hours / 24 + 1
    }

    fun getDayName(day: Int, locale: Locale?): String {
        val symbols = DateFormatSymbols(locale)
        val dayNames: Array<String> = symbols.shortWeekdays
        return dayNames[day]
    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayIntDate(): Int {
        return SimpleDateFormat("yyyyMMdd").format(Date()).toInt()
    }

    fun getHoursUTC(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getHoursLocal(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getMinutesUTC(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        return calendar.get(Calendar.MINUTE)
    }

    fun getSecondUTC(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        return calendar.get(Calendar.SECOND)
    }

    fun getShortDateUTC(time: Long): Int {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(time).toInt()
    }

    fun convertToLong(hour: Int, minute: Int, second: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        return calendar.timeInMillis
    }

    fun getStartEndTime(currentTime: Long): Pair<Long, Long> {
        val startTime: Long
        val endTime: Long
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.timeZone = TimeZone.getTimeZone("UTC")

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("UTC")
        if (minute in (0..29)) {
            cal.set(year, month, day, hour, 0, 0)
            cal.set(Calendar.MILLISECOND, 0)
            startTime = cal.timeInMillis

            cal.set(year, month, day, hour, 29, 59)
            cal.set(Calendar.MILLISECOND, 999)
            endTime = cal.timeInMillis
        } else {
            cal.set(year, month, day, hour, 30, 0)
            cal.set(Calendar.MILLISECOND, 0)
            startTime = cal.timeInMillis

            cal.set(year, month, day, hour, 59, 59)
            cal.set(Calendar.MILLISECOND, 999)
            endTime = cal.timeInMillis
        }
        return Pair(startTime, endTime)
    }

    fun getStartHourOfDay(currentTime: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getLastHour(currentTime: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        return calendar.timeInMillis
    }

    fun getStartEndTimePerHourUTCTime(currentTime: Long): Pair<Long, Long> {
        return getStartEndTimePerHourWithTimeZone(currentTime, TimeZone.getTimeZone("UTC"))
    }

    fun getStartEndTimePerHourLocalTime(currentTime: Long): Pair<Long, Long> {
        return getStartEndTimePerHourWithTimeZone(currentTime, TimeZone.getDefault())
    }

    private fun getStartEndTimePerHourWithTimeZone(
        currentTime: Long,
        timezone: TimeZone
    ): Pair<Long, Long> {
        val startTime: Long
        val endTime: Long
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.timeZone = timezone

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val cal = Calendar.getInstance()
        cal.timeZone = timezone
        cal.set(year, month, day, hour, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        startTime = cal.timeInMillis

        cal.set(year, month, day, hour, 59, 59)
        cal.set(Calendar.MILLISECOND, 999)
        endTime = cal.timeInMillis
        return Pair(startTime, endTime)
    }

    fun getStartEndTimePerDayUTCTime(currentTime: Long): Pair<Long, Long> {
        return getStartEndTimePerDayWithTimeZone(currentTime, TimeZone.getTimeZone("UTC"))
    }

    fun getStartEndTimePerDayLocalTime(currentTime: Long): Pair<Long, Long> {
        return getStartEndTimePerDayWithTimeZone(currentTime, TimeZone.getDefault())
    }

    private fun getStartEndTimePerDayWithTimeZone(
        currentTime: Long,
        timezone: TimeZone
    ): Pair<Long, Long> {
        val startTime: Long
        val endTime: Long
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.timeZone = timezone

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val cal = Calendar.getInstance()
        cal.timeZone = timezone
        cal.set(year, month, day, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        startTime = cal.timeInMillis

        cal.set(year, month, day, 23, 59, 59)
        cal.set(Calendar.MILLISECOND, 999)
        endTime = cal.timeInMillis
        return Pair(startTime, endTime)
    }

    fun getStartEndTimePerMonthUTCTime(currentTime: Long): Pair<Long, Long> {
        return getStartEndTimePerMonthWithTimeZone(currentTime, TimeZone.getTimeZone("UTC"))
    }

    fun getStartEndTimePerMonthLocalTime(currentTime: Long): Pair<Long, Long> {
        return getStartEndTimePerMonthWithTimeZone(currentTime, TimeZone.getDefault())
    }

    private fun getStartEndTimePerMonthWithTimeZone(
        currentTime: Long,
        timezone: TimeZone
    ): Pair<Long, Long> {
        val startTime : Long
        val endTime : Long
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.timeZone = timezone

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val cal = Calendar.getInstance()
        cal.timeZone = timezone
        cal.set(year, month, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        startTime = cal.timeInMillis

        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.MILLISECOND, -1)
        endTime = cal.timeInMillis
        return Pair(startTime, endTime)
    }

    fun getTimeHHmm(timeMilSecond: Long): String {
        val timeInSeconds = timeMilSecond / 1000
        var diffMinutes: Long = timeInSeconds / 60 % 60
        var diffHours: Long = timeInSeconds / (60 * 60) % 24
        if (diffMinutes < 0) diffMinutes = 0
        if (diffHours < 0) diffHours = 0

        return String.format(Locale.US, "%02d:%02d", diffHours, diffMinutes)
    }

    fun getTimeHHmmss(timeMilSecond: Long): String {
        val timeInSeconds = timeMilSecond / 1000
        var diffMinutes: Long = timeInSeconds / 60 % 60
        var diffHours: Long = timeInSeconds / (60 * 60)
        if (diffMinutes < 0) diffMinutes = 0
        if (diffHours < 0) diffHours = 0

        return String.format(Locale.US, "%dh %dm", diffHours, diffMinutes)
    }

    @SuppressLint("ResourceType")
    fun getTimeHHmmssFullText(timeMilSecond: Long, context: Context): String {
        val timeInSeconds = timeMilSecond / 1000
        var diffMinutes: Long = timeInSeconds / 60 % 60
        var diffHours: Long = timeInSeconds / (60 * 60)
        if (diffMinutes < 0) diffMinutes = 0
        if (diffHours < 0) diffHours = 0


        val unitMinutes = context.resources.getQuantityString(
            R.plurals.all_unit_minute,
            diffMinutes.toInt()
        )

        return if (diffHours > 0) {
            val unitHour = context.resources.getQuantityString(
                R.plurals.all_unit_hour,
                diffHours.toInt()
            )
            String.format(Locale.US, "%d $unitHour %d $unitMinutes", diffHours, diffMinutes)
        } else {
            String.format(Locale.US, "%d $unitMinutes", diffMinutes)
        }

    }

    @SuppressLint("ResourceType")
    fun getTimeHHmmNoZeroHour(timeMilSecond: Long, context: Context): String {
        val timeInSeconds = timeMilSecond / 1000
        var diffMinutes: Long = timeInSeconds / 60 % 60
        var diffHours: Long = timeInSeconds / (60 * 60)
        if (diffMinutes < 0) diffMinutes = 0
        if (diffHours < 0) diffHours = 0

        return if (diffHours > 0) {
            String.format(Locale.US, "%dh %dm", diffHours, diffMinutes)
        } else {
            val minString = context.getString(R.string.all_min)
            String.format(Locale.US, "%d $minString", diffMinutes)
        }
    }

    @SuppressLint("ResourceType")
    fun getListStringTimeDisplay(timeMilSecond: Long, context: Context): ListStringTimeDisplay {
        val timeInSeconds = timeMilSecond / 1000
        var diffMinutes: Long = timeInSeconds / 60 % 60
        var diffHours: Long = timeInSeconds / (60 * 60)
        if (diffMinutes < 0) diffMinutes = 0
        if (diffHours < 0) diffHours = 0

        return ListStringTimeDisplay(diffHours.toInt(), diffMinutes.toInt(), context)
    }

    fun getDayOfWeek(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getDayOfMonth(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getMonthOfYear(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return calendar.get(Calendar.MONTH)
    }

    fun convertShortDateToMs(shortDate: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.time = convertShortDateToDate(shortDate) ?: Date()
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    @SuppressLint("SimpleDateFormat")
    fun convertShortDateToDate(shortDate: Int): Date? {
        val sdf = SimpleDateFormat("yyyyMMdd")
        var parse: Date? = null
        try {
            parse = sdf.parse(shortDate.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return parse
    }
}