package com.jarvis.bmihealth.domain.usecase

import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository

class UserProfileUseCase(
    private var profileUserRepository: ProfileUserRepository
) {
    suspend fun deleteProfileUser(profileUser: ProfileUserModel) {
        profileUserRepository.deleteProfile(ProfileUserModel.convertModelToEntity(profileUser))
    }

    suspend fun deleteProfileUser(id: Int) {
        profileUserRepository.deleteProfile(id)
    }

    suspend fun getUserProfile(id: Int): ProfileUserModel? {
        return profileUserRepository.getProfileById(id)
            ?.let { ProfileUserModel.convertEntityToModel(it) }
    }

    suspend fun getAllUserProfile(): List<ProfileUserModel> {
        return profileUserRepository.getAllProfile()?.map {
            return@map ProfileUserModel.convertEntityToModel(it)
        } ?: emptyList()
    }

    suspend fun insertProfileUser(profileUserModel: ProfileUserModel) {
        profileUserRepository.insertProfile(ProfileUserModel.convertModelToEntity(profileUserModel))
    }

    suspend fun updateProfileUser(profileUserModel: ProfileUserModel) {
        profileUserRepository.updateProfile(ProfileUserModel.convertModelToEntity(profileUserModel))
    }
}
