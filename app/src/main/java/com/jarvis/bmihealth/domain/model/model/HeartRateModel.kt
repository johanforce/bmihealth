package com.jarvis.bmihealth.domain.model.model

import android.os.Parcelable
import com.jarvis.bmihealth.common.enums.HeartRateEnum
import com.jarvis.bmihealth.domain.model.entity.HeartRateEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeartRateModel(
    var idHeartRate: Int = 0,
    val id: Int = 0,
    val heartRate: Int = 0,
    val breath: Int = 0,
    val sPo2: Int = 0,
    var activity: Int = HeartRateEnum.GENERAL.activity,
    val dateTime: Long = 0L,
    var note: String = "",
) : Parcelable {
    companion object {
        fun convertEntityToModel(entity: HeartRateEntity?): HeartRateModel? {
            if (entity == null) return null
            return HeartRateModel(
                id = entity.id,
                idHeartRate = entity.idHeartRate,
                heartRate = entity.heartRate,
                breath = entity.breath,
                sPo2 = entity.sPo2,
                activity = entity.activity,
                dateTime = entity.dateTime,
                note = entity.note
            )
        }

        fun convertModelToEntity(model: HeartRateModel): HeartRateEntity {
            return HeartRateEntity(
                id = model.id,
                idHeartRate = model.idHeartRate,
                heartRate = model.heartRate,
                breath = model.breath,
                sPo2 = model.sPo2,
                activity = model.activity,
                dateTime = model.dateTime,
                note = model.note
            )
        }
    }
}
