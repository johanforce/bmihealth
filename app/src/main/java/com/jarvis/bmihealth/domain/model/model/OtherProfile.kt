package com.jarvis.bmihealth.domain.model.model

import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.bmihealth.common.enums.GenderEnum
import com.jarvis.heathcarebmi.utils.ActivityLevel
import com.jarvis.heathcarebmi.utils.GoalType
/**
 * Created by Jarvis Nguyen on 020/09/2022.
 */

@Suppress("unused")
class OtherProfile {
    var userId: Int? = null

    var lastName: String? = "Care"

    var firstName: String? = "Health"

    var avatar: String? = null

    var birthday:Long? = 788961600000L

    var gender: Int = GenderEnum.MALE.index

    var unit: Int = TypeUnits.METRIC.index

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
