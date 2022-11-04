package com.jarvis.bmihealth.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HeartRateEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_heart_rate")
    var idHeartRate: Int = 0,
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "heart_rate")
    val heartRate: Int = 0,
    @ColumnInfo(name = "activity")
    val activity: Int = 0,
    @ColumnInfo(name = "breath")
    val breath: Int = 0,
    @ColumnInfo(name = "sp_o2")
    val sPo2: Int = 0,
    @ColumnInfo(name = "date_time")
    val dateTime: Long = 0L,
    @ColumnInfo(name = "note")
    val note: String = "",
)
