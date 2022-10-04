package com.jarvis.bmihealth.domain.model

import com.jarvis.bmihealth.presentation.pref.ThemeMode.LIGHT
import com.jarvis.bmihealth.presentation.utilx.ActivityEnum
import com.jarvis.bmihealth.presentation.utilx.GoalEnum
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
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
    var unit: Int = METRIC,
    var height: Double = 0.0,
    var bio: String = "",
    var national: String = "",
    var goal: Int? = GoalEnum.MAINTAIN_WEIGHT.index,
    var activityLevel: Int? = ActivityEnum.MODERATELY.index,
    var themeMode: Int? = LIGHT
) : Serializable {
    companion object {
        fun convertEntityToModel(entity: ProfileUser): ProfileUserModel {
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

        fun convertModelToEntity(model: ProfileUserModel): ProfileUser {
            return ProfileUser(
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
                themeMode = model.themeMode ?: LIGHT
            )
        }

        fun convertListEntityToListModel(entities: List<ProfileUser>): List<ProfileUserModel> {
            val models = mutableListOf<ProfileUserModel>()
            entities.forEach {
                models.add(convertEntityToModel(it))
            }
            return models.toList()
        }
    }
}
