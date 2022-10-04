package com.jarvis.bmihealth.presentation.utilx

import com.jarvis.bmihealth.MainApplication
import com.jarvis.bmihealth.R

enum class GoalEnum(val index: Int,val title: String,val desMetric: String,val desImperial: String) {
    WEIGHT_LOSS_III(
        0,
        MainApplication.applicationContext().getString(R.string.onboarding_strict_loos),
        MainApplication.applicationContext().getString(R.string.goal_des_metric_1),
        MainApplication.applicationContext().getString(R.string.goal_des_imperial_1)
    ),
    WEIGHT_LOSS_II(
        1,
        MainApplication.applicationContext().getString(R.string.onboarding_mormal_weight),
        MainApplication.applicationContext().getString(R.string.goal_des_metric_2),
        MainApplication.applicationContext().getString(R.string.goal_des_imperial_2)
    ),
    WEIGHT_LOSS_I(
        2,
        MainApplication.applicationContext().getString(R.string.onboarding_comfortable),
        MainApplication.applicationContext().getString(R.string.goal_des_metric_3),
        MainApplication.applicationContext().getString(R.string.goal_des_imperial_3)
    ),
    MAINTAIN_WEIGHT(
        3,
        MainApplication.applicationContext().getString(R.string.maintain_weight),
        "",
        ""
    ),
    WEIGHT_GAIN_I(
        4,
        MainApplication.applicationContext().getString(R.string.onboarding_normal),
        MainApplication.applicationContext().getString(R.string.goal_des_metric_5),
        MainApplication.applicationContext().getString(R.string.goal_des_imperial_5)
    ),
    WEIGHT_GAIN_II(
        5, MainApplication.applicationContext().getString(R.string.onboarding_strict),
        MainApplication.applicationContext().getString(R.string.goal_des_metric_6),
        MainApplication.applicationContext().getString(R.string.goal_des_imperial_6)
    );
}