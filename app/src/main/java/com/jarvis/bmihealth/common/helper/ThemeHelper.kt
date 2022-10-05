package com.jarvis.bmihealth.common.helper

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.jarvis.bmihealth.common.enums.ThemeMode

object ThemeHelper {
    fun applyTheme(mode: Int) {
        when (mode) {
            ThemeMode.LIGHT.index -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            ThemeMode.DARK.index -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            ThemeMode.FOLLOW_SYSTEM.index -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
    }
}
