package com.jarvis.bmihealth.domain.repository

import com.jarvis.bmihealth.domain.model.entity.HeartRateEntity

@Suppress("unused")
interface HeartRateRepository {
    suspend fun getHeartRateById(id: Int): HeartRateEntity?

    suspend fun getAllHeartRate(): List<HeartRateEntity>?

    suspend fun getAllHeartRateInTime(startDate: Long, endDate: Long): List<HeartRateEntity>?

    suspend fun insertHeartRate(profile: HeartRateEntity)

    suspend fun updateHeartRate(profile: HeartRateEntity)

    suspend fun deleteHeartRate(profile: HeartRateEntity)

    suspend fun deleteHeartRate(id: Int)

    suspend fun getLastResultHeartRate(): HeartRateEntity?
}
