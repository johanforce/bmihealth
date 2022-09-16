package com.jarvis.bmihealth.presentation.utilx

import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
import com.jarvis.heathcarebmi.utils.ActivityLevel
import com.jarvis.heathcarebmi.utils.GoalType
/**
 * Created by Domingo Luan on 08/18/2021.
 */

@Suppress("unused")
class OtherProfile {
    var userId: Int? = null

    var lastName: String? = null

    var firstName: String? = null

    var avatar: String? = null

    var birthday:Long? = 788961600000L

    var gender: Int = Gender.MALE

    var unit: Int = METRIC

    var userType: Int = 0

    var goal: Int? = GoalType.MAINTAIN_WEIGHT

    var activityLevel: Int? = ActivityLevel.MODERATELY

    var heightInCentimeters: Double = getHeightInCentimetersDefault(gender)

    var weightInKilograms: Double = getWeightInKilogramsDefault(gender)

    private fun getHeightInCentimetersDefault(gender: Int): Double {
        val listHeightDefault = listOf(180.0, 165.0, 170.0)
        return listHeightDefault[gender]
    }
    private fun getWeightInKilogramsDefault(gender: Int): Double {
        val listWeightDefault = listOf(75.0, 60.0, 68.0)
        return listWeightDefault[gender]
    }

}