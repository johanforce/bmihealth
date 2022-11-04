package com.jarvis.bmihealth.common.utils

import kotlin.math.sqrt

object HealthUtils {
    //Time heart rate monitor
    const val TIME_COUNT_HEART_RATE = 15
    const val TIME_SECONDS_START_MONITOR = 10
    const val MILLISECONDS_PER_SECOND = 1000.0
    const val SECOND_PER_MINUTE = 60
    const val BPM_MIN_MONITOR = 45
    const val BPM_MAX_MONITOR = 200
    const val RED_DOTS_PER_FRAME = 200
    const val BREATH_MIN_PER_SECOND = 10
    const val BREATH_MAX_PER_SECOND = 50

    fun calculateSPo2(
        standardRed: Double,
        standardBlue: Double,
        measurementRed: Double,
        measurementBlue: Double,
        counter: Int
    ): Int {
        //calculating the variance
        val varRedO2 = sqrt(standardRed / (counter - 1))
        val varBlueO2 = sqrt(standardBlue / (counter - 1))
        //giá trị tỉ lệ trung bình phương sai điểm đỏ và xanh
        val ratioAvgVarianceO2 = (varRedO2 / measurementRed) / (varBlueO2 / measurementBlue)
        //estimating SPo2
        val spo2 = 100 - 5 * ratioAvgVarianceO2
        return spo2.toInt()
    }
}
