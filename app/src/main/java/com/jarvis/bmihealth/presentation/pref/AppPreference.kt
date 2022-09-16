package com.jarvis.bmihealth.presentation.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.jarvis.bmihealth.MainApplication


class AppPreference private constructor() {
    private var mSharedPreferences: SharedPreferences
    private val PREFS_NAME = "app_pref"

    init {
        mSharedPreferences =
            MainApplication.applicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private object Holder {
        val INSTANCE = AppPreference()
    }

    companion object {
        @JvmStatic
        fun getInstance(): AppPreference {
            return Holder.INSTANCE
        }
    }


    operator fun <T> get(key: String?, anonymousClass: Class<T>): T? {
        try {
            if (anonymousClass == String::class.java) {
                return mSharedPreferences.getString(key, "") as T
            } else if (anonymousClass == Boolean::class.javaObjectType) {
                return java.lang.Boolean.valueOf(mSharedPreferences.getBoolean(key, false)) as T
            } else if (anonymousClass == Boolean::class.java) {
                return java.lang.Boolean.valueOf(mSharedPreferences.getBoolean(key, false)) as T
            } else if (anonymousClass == Float::class.java) {
                return java.lang.Float.valueOf(mSharedPreferences.getFloat(key, 0f)) as T
            } else if (anonymousClass == Int::class.java || anonymousClass == Integer::class.java) {
                return Integer.valueOf(mSharedPreferences.getInt(key, 0)) as T
            } else if (anonymousClass == Long::class.java) {
                return java.lang.Long.valueOf(mSharedPreferences.getLong(key, 0)) as T
            } else {
                return Gson().fromJson(mSharedPreferences.getString(key, ""), anonymousClass)
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
            return null
        }
    }

    fun <T> put(key: String?, data: T) {
        val editor = mSharedPreferences.edit()
        if (data is String) {
            editor.putString(key, data as String)
        } else if (data is Boolean) {
            editor.putBoolean(key, (data as Boolean))
        } else if (data is Float) {
            editor.putFloat(key, (data as Float))
        } else if (data is Int) {
            editor.putInt(key, (data as Int))
        } else if (data is Long) {
            editor.putLong(key, (data as Long))
        } else {
            editor.putString(key, MainApplication.applicationContext().gson.toJson(data));
        }
        editor.apply()
    }

    fun <T> putSync(key: String?, data: T) {
        val editor = mSharedPreferences.edit()
        if (data is String) {
            editor.putString(key, data as String)
        } else if (data is Boolean) {
            editor.putBoolean(key, (data as Boolean))
        } else if (data is Float) {
            editor.putFloat(key, (data as Float))
        } else if (data is Int) {
            editor.putInt(key, (data as Int))
        } else if (data is Long) {
            editor.putLong(key, (data as Long))
        } else {
            editor.putString(key, MainApplication.applicationContext().gson.toJson(data));
        }
        editor.commit()
    }

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }
}