@file:Suppress("DEPRECATION", "unused", "MemberVisibilityCanBePrivate")

package com.jarvis.bmihealth.presentation.utilx

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import java.util.*

object DeviceUtil {

    fun openMarket(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=" + context.packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun intentToWebsite(context: Context) {
        try {
            val url = "https://mywelltraining.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun intentToFacebook(context: Context) {
        val url = "https://www.facebook.com/WellTraining.sports"
        val uri: Uri? = try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            Uri.parse("fb://facewebmodal/f?href=$url")
        } catch (e: PackageManager.NameNotFoundException) {
            Uri.parse(url)
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setStatusBarColor(context: Activity, @ColorRes colorRes: Int) {
        try {
            val window = context.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = context.resources.getColor(colorRes)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun showStatusBarBlack(activity: Activity?, isShowBlack: Boolean) {
        var colorStatusBar = R.color.white_5
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            colorStatusBar = R.color.status_bar_android5
        }
        if (activity != null) {
            setStatusBarColor(activity, if (isShowBlack) R.color.black_4 else colorStatusBar)
        }
    }

    fun showStatusBarBilling(activity: Activity?) {
        if (activity != null) {
            setStatusBarColor(activity, R.color.color_onboading_status)
        }
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

    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    fun isConnected(context: Context?): Boolean {
//        NetworkInfo info = getNetworkInfo(context);
        return isNetworkAvailable(context)
    }

    fun isConnectedAndToast(context: Context): Boolean {
//        NetworkInfo info = getNetworkInfo(context);
        val isConnected = isNetworkAvailable(context)
        if (!isConnected) {
            Toast.makeText(
                context,
                context.getString(R.string.all_network_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isConnected
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    true
                } else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } else {
            try {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            } catch (ignored: Exception) {
            }
        }
        return false
    }

    fun isAllowPermission(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionGrantedApi29(context)
        } else true
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    fun checkPermissionGrantedApi29(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isActivityRecognitionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
                    == PackageManager.PERMISSION_GRANTED)
        } else true
    }

    fun getCountryCode(context: Context): String {
        val telephoneManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephoneManager.networkCountryIso
        return if (TextUtils.isEmpty(countryCode)) {
            context.resources.configuration.locale.country.uppercase(Locale.getDefault())
        } else countryCode.uppercase(Locale.getDefault())
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

    fun wordFirstCap(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return "Unknown"
        }
        val words = str.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
        val ret = StringBuilder()
        for (i in words.indices) {
            if (words[i].trim { it <= ' ' }.isNotEmpty()) {
                ret.append(words[i].trim { it <= ' ' }[0].uppercaseChar())
                ret.append(words[i].trim { it <= ' ' }.substring(1))
                if (i < words.size - 1) {
                    ret.append(' ')
                }
            }
        }
        return ret.toString()
    }
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