package com.jarvis.bmihealth.domain.usecase

import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.domain.repository.HeartRateRepository

class HeartRateUseCase(
    private var heartRateRepository: HeartRateRepository
) {
    suspend fun deleteHeartRate(heartRateModel: HeartRateModel) {
        heartRateRepository.deleteHeartRate(HeartRateModel.convertModelToEntity(heartRateModel))
    }

    suspend fun deleteHeartRate(id: Int) {
        heartRateRepository.deleteHeartRate(id)
    }

    suspend fun getHeartRate(id: Int): HeartRateModel? {
        return heartRateRepository.getHeartRateById(id)
            ?.let { HeartRateModel.convertEntityToModel(it) }
    }

    suspend fun getAllHeartRate(): List<HeartRateModel?> {
        return heartRateRepository.getAllHeartRate()?.map {
            return@map HeartRateModel.convertEntityToModel(it)
        } ?: emptyList()
    }

    suspend fun getAllHeartRateInTime(startDate: Long, endDate: Long): List<HeartRateModel?> {
        return heartRateRepository.getAllHeartRateInTime(startDate, endDate)?.map {
            return@map HeartRateModel.convertEntityToModel(it)
        } ?: emptyList()
    }

    suspend fun insertHeartRate(heartRateModel: HeartRateModel) {
        return heartRateRepository.insertHeartRate(
            HeartRateModel.convertModelToEntity(
                heartRateModel
            )
        )
    }

    suspend fun updateHeartRate(heartRateModel: HeartRateModel) {
        return heartRateRepository.updateHeartRate(
            HeartRateModel.convertModelToEntity(
                heartRateModel
            )
        )
    }

    suspend fun getLastResultHeartRate(): HeartRateModel? {
        return HeartRateModel.convertEntityToModel(
            heartRateRepository.getLastResultHeartRate()
        )
    }
}
