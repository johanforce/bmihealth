package com.jarvis.bmihealth.domain.use_case

import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.NoteRepository

class UserProfileUseCase(
    private var noteRepository: NoteRepository
) {
    suspend fun deleteProfileUser(profileUser: ProfileUser) {
        noteRepository.deleteProfile(profileUser)
    }

    suspend fun getUserProfile(id: Int) {
        noteRepository.getProfileById(id)
    }

    suspend fun insertProfileUser(profileUser: ProfileUser) {
        noteRepository.insertProfile(profileUser)
    }

    suspend fun updateProfileUser(profileUser: ProfileUser) {
        noteRepository.updateProfile(profileUser)
    }
}