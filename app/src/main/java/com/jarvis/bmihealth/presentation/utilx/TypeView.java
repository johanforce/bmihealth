package com.jarvis.bmihealth.presentation.utilx;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TypeView.height, TypeView.weight, TypeView.strideLength})
@Retention(RetentionPolicy.SOURCE)
public @interface TypeView {
    int height = 0;
    int weight = 1;
    int strideLength = 2;
}
