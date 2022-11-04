package com.jarvis.bmihealth.domain.model.model

import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.bmihealth.common.enums.ActivityEnum
import com.jarvis.bmihealth.common.enums.GoalEnum
import com.jarvis.bmihealth.common.enums.ThemeMode
import com.jarvis.bmihealth.domain.model.entity.ProfileUserEntity
import java.io.Serializable

data class ProfileUserModel(
    var id: Int = 0,
    var firstname: String = "",
    var lastname: String = "",
    var gender: Int = 0,
    var birthday: Long = 0L,
    var age: Int = 0,
    var avatar: ByteArray? = ByteArray(0),
    var avatarUrl: String = "",
    var weight: Double = 0.0,
    var unit: Int = TypeUnits.METRIC.index,
    var height: Double = 0.0,
    var bio: String = "",
    var national: String = "",
    var goal: Int? = GoalEnum.MAINTAIN_WEIGHT.index,
    var activityLevel: Int? = ActivityEnum.MODERATELY.index,
    var themeMode: Int? = ThemeMode.LIGHT.index,
) : Serializable {
    companion object {
        fun convertEntityToModel(entity: ProfileUserEntity): ProfileUserModel {
            return ProfileUserModel(
                id = entity.id,
                firstname = entity.firstname,
                gender = entity.gender,
                lastname = entity.lastname,
                birthday = entity.birthday,
                age = entity.age,
                avatar = entity.avatar,
                avatarUrl = entity.avatarUrl,
                weight = entity.weight,
                height = entity.height,
                bio = entity.bio,
                unit = entity.unit,
                national = entity.national,
                goal = entity.goal,
                activityLevel = entity.activityLevel,
                themeMode = entity.themeMode
            )
        }

        fun convertModelToEntity(model: ProfileUserModel): ProfileUserEntity {
            return ProfileUserEntity(
                id = model.id,
                firstname = model.firstname,
                gender = model.gender,
                lastname = model.lastname,
                birthday = model.birthday,
                age = model.age,
                avatar = model.avatar,
                avatarUrl = model.avatarUrl,
                weight = model.weight,
                height = model.height,
                bio = model.bio,
                unit = model.unit,
                national = model.national,
                goal = model.goal ?: 0,
                activityLevel = model.activityLevel ?: 0,
                themeMode = model.themeMode ?: ThemeMode.LIGHT.index
            )
        }
    }
}
