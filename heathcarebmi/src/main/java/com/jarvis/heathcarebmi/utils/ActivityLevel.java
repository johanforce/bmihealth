package com.jarvis.heathcarebmi.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ActivityLevel.SEDENTARY, ActivityLevel.LIGHTLY, ActivityLevel.MODERATELY,
        ActivityLevel.VERY_ACTIVE, ActivityLevel.EXTREMELY_ACTIVE})
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityLevel {
    int SEDENTARY = 1;
    int LIGHTLY = 2;
    int MODERATELY = 3;
    int VERY_ACTIVE = 4;
    int EXTREMELY_ACTIVE = 5;
}
