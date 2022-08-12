package com.jarvis.bmihealth.data.repository

import com.jarvis.bmihealth.data.datasource.ProfileUserDao
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository

class ProfileUserRepositoryImpl(private val profileUserDao: ProfileUserDao) :
    ProfileUserRepository {
    override suspend fun getProfileById(id: Int): ProfileUser? {
        return profileUserDao.getNoteById(id)
    }

    override suspend fun insertProfile(profile: ProfileUser) {
        profileUserDao.insertNote(profile)
    }

    override suspend fun updateProfile(profile: ProfileUser) {
        profileUserDao.updateNote(profile)
    }

    override suspend fun deleteProfile(profile: ProfileUser) {
        profileUserDao.deleteNote(profile)
    }

}
