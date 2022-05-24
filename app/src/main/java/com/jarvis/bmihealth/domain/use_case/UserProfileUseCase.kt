package com.jarvis.bmihealth.domain.use_case

import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository

class UserProfileUseCase(
    private var profileUserRepository: ProfileUserRepository
) {
    suspend fun deleteProfileUser(profileUser: ProfileUser) {
        profileUserRepository.deleteProfile(profileUser)
    }

    suspend fun getUserProfile(id: Int) {
        profileUserRepository.getProfileById(id)
    }

    suspend fun insertProfileUser(profileUser: ProfileUser) {
        profileUserRepository.insertProfile(profileUser)
    }

    suspend fun updateProfileUser(profileUser: ProfileUser) {
        profileUserRepository.updateProfile(profileUser)
    }
}