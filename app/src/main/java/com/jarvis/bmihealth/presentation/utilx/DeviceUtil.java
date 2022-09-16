package com.jarvis.bmihealth.presentation.utilx;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jarvis.bmihealth.R;

public class DeviceUtil {

    public static void openMarket(Context context, String packageName) {
        try {
            if (TextUtils.isEmpty(packageName)) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openMarket(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void share(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context.startActivity(Intent.createChooser(intent, context.getString(R.string.about_share)));
    }

    public static void intentToWebsite(Context context) {
        try {
            String url = "https://mywelltraining.com";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void intentToFacebook(Context context) {
        String url = "https://www.facebook.com/WellTraining.sports";
        Uri uri;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStatusbarColor(Activity context, @ColorRes int colorRes) {
        try {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(context.getResources().getColor(colorRes));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void showStatusbarBlack(Activity activity, boolean isShowBlack) {
        int colorStatusbar = R.color.white_5;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            colorStatusbar = R.color.status_bar_android5;
        }
        if (activity != null) {
            DeviceUtil.setStatusbarColor(activity, isShowBlack ? R.color.black_4 : colorStatusbar);
        }
    }

    public static void showStatusBarBilling(Activity activity) {
        if (activity != null) {
            DeviceUtil.setStatusbarColor(activity, R.color.color_onboading_status);
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isPackageExisted(Context context, String targetPackage) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }


    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }

    public static boolean isConnected(Context context) {
//        NetworkInfo info = getNetworkInfo(context);
        return isNetworkAvailable(context);
    }

    public static boolean isConnectedAndToast(Context context) {
//        NetworkInfo info = getNetworkInfo(context);
        boolean isConnected = isNetworkAvailable(context);
        if (!isConnected) {
            Toast.makeText(context, context.getString(R.string.all_network_error_message), Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    private static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    public static boolean isAllowPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return checkPermissionGrantedApi29(context);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean checkPermissionGrantedApi29(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isActivityRecognitionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public static String getCountryCode(Context context) {
        TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = telephoneManager.getNetworkCountryIso();
        if (TextUtils.isEmpty(countryCode)) {
            return context.getResources().getConfiguration().locale.getCountry().toUpperCase();
        }
        return countryCode.toUpperCase();
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 400;
    }

    public static String wordFirstCap(String str) {
        if (TextUtils.isEmpty(str)) {
            return "Unknown";
        }
        String[] words = str.trim().split(" ");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (words[i].trim().length() > 0) {
                ret.append(Character.toUpperCase(words[i].trim().charAt(0)));
                ret.append(words[i].trim().substring(1));
                if (i < words.length - 1) {
                    ret.append(' ');
                }
            }
        }

        return ret.toString();
    }
}
