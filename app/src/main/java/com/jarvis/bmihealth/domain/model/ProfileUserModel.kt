package com.jarvis.bmihealth.domain.model

import com.jarvis.heathcarebmi.utils.ActivityLevel
import com.jarvis.heathcarebmi.utils.GoalType
import java.io.Serializable

data class ProfileUserModel(
    val firstname: String,
    val lastname: String,
    val gender: Int,
    val birthday: Long,
    val age: Int,
    val avatar: ByteArray?,
    val avatarUrl: String,
    val weight: Double,
    val unit: Int,
    val height: Double,
    val bio: String,
    val national: String,
    var goal: Int? = GoalType.MAINTAIN_WEIGHT,
    var activityLevel: Int? = ActivityLevel.MODERATELY
): Serializable {
    companion object {
        fun convertEntityToModel(entity: ProfileUser): ProfileUserModel {
            return ProfileUserModel(
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
                activityLevel = entity.activityLevel
            )
        }

        fun convertModelToEntity(model: ProfileUserModel): ProfileUser {
            return ProfileUser(
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
                goal = model.goal?:0,
                activityLevel = model.activityLevel?:0
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
