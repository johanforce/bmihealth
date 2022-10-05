package com.jarvis.bmihealth.common.enums

import com.jarvis.bmihealth.MainApplication
import com.jarvis.bmihealth.R

enum class ActivityEnum(val index: Int,val title: String,val description: String) {
    SEDENTARY(
        0,
        MainApplication.applicationContext().getString(R.string.rpe_sedentary),
        MainApplication.applicationContext().getString(R.string.rpe_sedentary_des)
    ),
    LIGHT_ACTIVE(
        1,
        MainApplication.applicationContext().getString(R.string.rpe_lightly),
        MainApplication.applicationContext().getString(R.string.rpe_lightly_des)
    ),
    MODERATELY(
        2, MainApplication.applicationContext().getString(R.string.rpe_moderately),
        MainApplication.applicationContext().getString(R.string.rpe_moderately_des)
    ),
    VERY_ACTIVE(
        3,
        MainApplication.applicationContext().getString(R.string.rpe_very_active),
        MainApplication.applicationContext().getString(R.string.rpe_very_active_des)
    ),
    EXTREMELY_ACTIVE(
        4,
        MainApplication.applicationContext().getString(R.string.rpe_extremely),
        MainApplication.applicationContext().getString(R.string.rpe_extremely_des)
    );
}