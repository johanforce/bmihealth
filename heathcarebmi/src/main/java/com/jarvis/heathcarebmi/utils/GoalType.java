package com.jarvis.heathcarebmi.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({GoalType.STRICT_LOSS_WEIGHT, GoalType.NORMAL_LOSS_WEIGHT, GoalType.COMFORTABLE_LOSS_WEIGHT,
        GoalType.MAINTAIN_WEIGHT, GoalType.NORMAL_GAIN_WEIGHT, GoalType.STRICT_GAIN_WEIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface GoalType {
    int STRICT_LOSS_WEIGHT = 1;
    int NORMAL_LOSS_WEIGHT = 2;
    int COMFORTABLE_LOSS_WEIGHT = 3;
    int MAINTAIN_WEIGHT = 4;
    int NORMAL_GAIN_WEIGHT = 5;
    int STRICT_GAIN_WEIGHT = 6;
}