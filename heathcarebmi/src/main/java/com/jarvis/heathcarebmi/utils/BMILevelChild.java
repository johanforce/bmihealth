package com.jarvis.heathcarebmi.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({BMILevelChild.BMI_1, BMILevelChild.BMI_2, BMILevelChild.BMI_3,
        BMILevelChild.BMI_4, BMILevelChild.BMI_5})
@Retention(RetentionPolicy.SOURCE)
public @interface BMILevelChild {
    int BMI_1 = 1;
    int BMI_2 = 2;
    int BMI_3 = 3;
    int BMI_4 = 4;
    int BMI_5 = 5;
}
