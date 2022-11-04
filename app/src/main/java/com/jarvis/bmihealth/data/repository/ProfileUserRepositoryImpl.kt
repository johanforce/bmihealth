package com.jarvis.bmihealth.data.repository

import com.jarvis.bmihealth.data.datasource.dao.ProfileUserDao
import com.jarvis.bmihealth.domain.model.entity.ProfileUserEntity
import com.jarvis.bmihealth.domain.repository.ProfileUserRepository

class ProfileUserRepositoryImpl(private val profileUserDao: ProfileUserDao) :
    ProfileUserRepository {
    override suspend fun getProfileById(id: Int): ProfileUserEntity? {
        return profileUserDao.getNoteById(id)
    }

    override suspend fun getAllProfile(): List<ProfileUserEntity>? {
        return profileUserDao.getAllProfile()
    }

    override suspend fun insertProfile(profile: ProfileUserEntity) {
        return profileUserDao.insertNote(profile)
    }

    override suspend fun updateProfile(profile: ProfileUserEntity) {
        return profileUserDao.updateProfile(profile)
    }

    override suspend fun deleteProfile(profile: ProfileUserEntity) {
        profileUserDao.deleteNote(profile)
    }

    override suspend fun deleteProfile(id: Int) {
        profileUserDao.deleteProfile(id)
    }
}
