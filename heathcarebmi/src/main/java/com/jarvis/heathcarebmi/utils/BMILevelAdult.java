package com.jarvis.heathcarebmi.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({BMILevelAdult.BMI_1, BMILevelAdult.BMI_2, BMILevelAdult.BMI_3,
        BMILevelAdult.BMI_4, BMILevelAdult.BMI_5,BMILevelAdult.BMI_6})
@Retention(RetentionPolicy.SOURCE)
public @interface BMILevelAdult {
    int BMI_1 = 1;
    int BMI_2 = 2;
    int BMI_3 = 3;
    int BMI_4 = 4;
    int BMI_5 = 5;
    int BMI_6 = 6;
}
