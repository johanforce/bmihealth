package com.jarvis.bmihealth.presentation.selectmode

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.jarvis.bmihealth.presentation.pref.ThemeMode

class ThemeHelper {
    companion object {
        fun applyTheme(mode: Int) {
            when (mode) {
                ThemeMode.LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                ThemeMode.DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                ThemeMode.FOLLOW_SYSTEM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
        }

    }
}