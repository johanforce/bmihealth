package com.jarvis.bmihealth.domain.repository

import com.jarvis.bmihealth.domain.model.ProfileUser

interface NoteRepository {

    suspend fun getProfileById(id:Int): ProfileUser?

    suspend fun insertProfile(profile: ProfileUser)

    suspend fun updateProfile(profile: ProfileUser)

    suspend fun deleteProfile(profile: ProfileUser)
}