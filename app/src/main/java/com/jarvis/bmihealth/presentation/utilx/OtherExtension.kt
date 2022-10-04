/*
 * Copyright © OMRON HEALTHCARE Co., Ltd. 2020. All rights reserved.
 */

@file:Suppress("DEPRECATION")

package com.jarvis.bmihealth.presentation.utilx

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.format.DateUtils
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

private const val MINUTE_COEFFICIENT = 60 * 1000
private val DECIMAL_LONG_FORMAT by lazy {
    StringBuilder("#.").apply {
        repeat(255) {
            append("#")
        }
    }.toString()
}

/**
 * This is the method to check network connection status
 */
fun Context?.isConnected(): Boolean {
    this ?: return false
    val cm: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.isConnected == true
}

/**
 * This is the method to open web browser
 */
fun Fragment.openBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url.trim())
    startActivity(intent)
}


/**
 * This is the method to send mail no file
 * @param email is address of mail to
 */
fun Fragment.sendMailNoFile(email: String?) {
    if (!email.isNullOrBlank()) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val listMail = arrayOf(email)
        intent.putExtra(Intent.EXTRA_EMAIL, listMail)
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        startActivity(intent)
    }
}

/**
 * This is the method to get drawable compat
 */
fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

/**
 * This is the method to get color compat
 */
fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

/**
 * This is the method to set text color
 */
fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

/**
 * Return difference minutes between 2 dates
 */
fun Date.minutesBetween(other: Date): Long {
    return other.time / MINUTE_COEFFICIENT - time / MINUTE_COEFFICIENT
}

/**
 * Return difference minutes between 2 dates
 */
fun Date.secondsBetween(other: Date): Long {
    return other.time / DateUtils.SECOND_IN_MILLIS - time / DateUtils.SECOND_IN_MILLIS
}

/**
 * Return difference minutes between 2 dates long
 */
fun Long.minutesBetween(other: Long): Long {
    return other / MINUTE_COEFFICIENT - this / MINUTE_COEFFICIENT
}

/**
 * extension function to change radio button circle color programmatically
 */
fun RadioButton.setCircleColor(color: Int) {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked) // checked
        ),
        intArrayOf(
            Color.GRAY, // unchecked color
            color // checked color
        )
    )
    // finally, set the radio button's button tint list
    buttonTintList = colorStateList
    // optionally set the button tint mode or tint blend mode
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        buttonTintBlendMode = BlendMode.SRC_IN
    } else {
        buttonTintMode = PorterDuff.Mode.SRC_IN
    }
    // could not be necessary
    invalidate()
}

fun Int.roundFloorGraph(): Int {
    if (this % 10 != 0) {
        return this / 10 * 10
    }
    return this - 10
}

fun Int.roundCellingGraph(): Int {
    if (this % 10 != 0) {
        return (this / 10 + 1) * 10
    }
    return this + 10
}

fun Float.roundFloorGraph(): Float {
    val intValue = toInt()
    val roundValue = if (intValue % 10 == 0) intValue else (intValue / 10) * 10
    return roundValue - 10f
}

fun Float.roundCellingGraph(): Float {
    val intValue = toInt()
    val roundValue = if (intValue % 10 == 0) intValue else (intValue / 10) * 10 + 10
    return roundValue + 10f
}

fun Number.roundCelling(numberDigitAfterDot: Int): String {
    if (numberDigitAfterDot <= 0) return formatCelling("#")
    val format = StringBuilder("#.").apply {
        repeat(numberDigitAfterDot) {
            append("#")
        }
    }.toString()
    return formatCelling(format)
}

fun Number.shorten(): String {
    val decimalFormat = DecimalFormat(DECIMAL_LONG_FORMAT)
    return decimalFormat.format(this)
}

fun Number.formatCelling(format: String): String {
    return kotlin.runCatching {
        DecimalFormat(format).apply {
            roundingMode = RoundingMode.CEILING
        }.format(this).toString()
    }.getOrNull() ?: ""
}

fun Date.copy(onEdit: Date.() -> Unit = {}) = Date(time).apply { onEdit() }
