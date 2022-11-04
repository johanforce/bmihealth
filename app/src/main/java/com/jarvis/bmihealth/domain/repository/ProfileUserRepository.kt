package com.jarvis.bmihealth.domain.repository

import com.jarvis.bmihealth.domain.model.entity.ProfileUserEntity

@Suppress("unused")
interface ProfileUserRepository {
    suspend fun getProfileById(id: Int): ProfileUserEntity?

    suspend fun getAllProfile(): List<ProfileUserEntity>?

    suspend fun insertProfile(profile: ProfileUserEntity)

    suspend fun updateProfile(profile: ProfileUserEntity)

    suspend fun deleteProfile(profile: ProfileUserEntity)

    suspend fun deleteProfile(id: Int)
}
