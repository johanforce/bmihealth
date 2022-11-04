@file:Suppress("DEPRECATION", "MemberVisibilityCanBePrivate", "unused")

package com.jarvis.bmihealth.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jarvis.heathcarebmi.utils.BMILevelAdult
import com.jarvis.heathcarebmi.utils.BMILevelChild
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object DeviceUtil {

    fun levelBMIAdult(bmi: Double): Int {
        return if (bmi < 16.0) {
            BMILevelAdult.BMI_1
        } else if (bmi >= 16 && bmi < 18.5) {
            BMILevelAdult.BMI_2
        } else if (bmi >= 18.5 && bmi < 25) {
            BMILevelAdult.BMI_3
        } else if (bmi >= 25 && bmi < 30) {
            BMILevelAdult.BMI_4
        } else if (bmi >= 30 && bmi < 35) {
            BMILevelAdult.BMI_5
        } else if (bmi > 35 && bmi < 40) {
            BMILevelAdult.BMI_6
        } else {
            BMILevelAdult.BMI_7
        }
    }

    fun levelBMChild(bmi: Double): Int {
        return if (bmi < 3.0) {
            BMILevelChild.BMI_1
        } else if (bmi >= 3.0 && bmi < 15.0) {
            BMILevelChild.BMI_2
        } else if (bmi >= 15 && bmi < 85) {
            BMILevelChild.BMI_3
        } else if (bmi >= 85 && bmi < 97) {
            BMILevelChild.BMI_4
        } else {
            BMILevelAdult.BMI_5
        }
    }

    fun roundOffDecimal(number: Double): String {
//        val numberString  = number.toString().replace(",",".")
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number)
    }

    fun share(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=" + context.packageName
        )
        intent.type = "text/plain"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        //        context.startActivity(Intent.createChooser(intent, context.getString(R.string.about_share)));
    }


    fun hideKeyboard(activity: Activity) {
        try {
            val manager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                activity.findViewById<View>(android.R.id.content).windowToken,
                0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showKeyBoard(activity: Activity, view: View?) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun isPackageExisted(context: Context, targetPackage: String?): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(targetPackage!!, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    fun getAppVersion(context: Context): Int {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 400
    }

    fun openMarket(context: Context, packageName: String) {
        try {
            if (TextUtils.isEmpty(packageName)) {
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun upCaseFirst(string: String): String? {
        return if (TextUtils.isEmpty(string) || string.length < 2) {
            string
        } else string.substring(0, 1).uppercase(Locale.getDefault()) + string.substring(1)
    }
}
