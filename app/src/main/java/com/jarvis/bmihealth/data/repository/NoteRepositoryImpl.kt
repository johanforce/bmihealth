package com.jarvis.bmihealth.data.repository

import com.jarvis.bmihealth.data.data_source.ProfileUserDao
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.NoteRepository

class NoteRepositoryImpl(private val noteDao: ProfileUserDao) : NoteRepository {
    override suspend fun getProfileById(id: Int): ProfileUser? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertProfile(profile: ProfileUser) {
        noteDao.insertNote(profile)
    }

    override suspend fun updateProfile(profile: ProfileUser) {
        noteDao.updateNote(profile)
    }

    override suspend fun deleteProfile(profile: ProfileUser) {
        noteDao.deleteNote(profile)
    }

}