package com.jarvis.bmihealth.data.repository

import com.jarvis.bmihealth.data.datasource.dao.HeartRateDao
import com.jarvis.bmihealth.domain.model.entity.HeartRateEntity
import com.jarvis.bmihealth.domain.repository.HeartRateRepository

class HeartRateRepositoryImpl(private val heartRateDao: HeartRateDao) :
    HeartRateRepository {
    override suspend fun getHeartRateById(id: Int): HeartRateEntity? {
        return heartRateDao.getNoteById(id)
    }

    override suspend fun getAllHeartRate(): List<HeartRateEntity>? {
        return heartRateDao.getAllHeartRate()
    }

    override suspend fun getAllHeartRateInTime(
        startDate: Long,
        endDate: Long
    ): List<HeartRateEntity>? {
        return heartRateDao.getAllHeartRateInTime(startDate, endDate)
    }

    override suspend fun insertHeartRate(profile: HeartRateEntity) {
        return heartRateDao.insertHeartRate(profile)
    }

    override suspend fun updateHeartRate(profile: HeartRateEntity) {
        return heartRateDao.updateHeartRate(profile)
    }

    override suspend fun deleteHeartRate(profile: HeartRateEntity) {
        heartRateDao.deleteHeartRate(profile)
    }

    override suspend fun deleteHeartRate(id: Int) {
        heartRateDao.deleteHeartRate(id)
    }

    override suspend fun getLastResultHeartRate(): HeartRateEntity? {
        return kotlin.runCatching {
            heartRateDao.getLastResultHeartRate()
        }.getOrNull()
    }
}
