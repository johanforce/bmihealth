package com.jarvis.bmihealth.data.datasource.dao

import androidx.room.*
import com.jarvis.bmihealth.domain.model.entity.HeartRateEntity

@Dao
interface HeartRateDao {
    @Query("select * from heartrateentity where id_heart_rate=:id")
    suspend fun getNoteById(id: Int): HeartRateEntity?

    @Query("select * from heartrateentity")
    suspend fun getAllHeartRate(): List<HeartRateEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeartRate(heartRateEntity: HeartRateEntity)

    @Update
    suspend fun updateHeartRate(heartRateEntity: HeartRateEntity)

    @Delete
    suspend fun deleteHeartRate(heartRateEntity: HeartRateEntity)

    @Query("delete from heartrateentity where id_heart_rate = :id")
    suspend fun deleteHeartRate(id: Int)

    @Query("SELECT * FROM heartrateentity ORDER BY date_time DESC LIMIT 1")
    suspend fun getLastResultHeartRate(): HeartRateEntity

    @Query("select * from heartrateentity where date_time >= :startDate and date_time <= :endDate")
    suspend fun getAllHeartRateInTime(startDate: Long, endDate: Long): List<HeartRateEntity>?
}
