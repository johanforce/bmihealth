package com.jarvis.bmihealth.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jarvis.bmihealth.data.datasource.dao.HeartRateDao
import com.jarvis.bmihealth.data.datasource.dao.ProfileUserDao
import com.jarvis.bmihealth.domain.model.entity.HeartRateEntity
import com.jarvis.bmihealth.domain.model.entity.ProfileUserEntity

@Database(
    entities = [ProfileUserEntity::class, HeartRateEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val profileUserDao: ProfileUserDao

    abstract val heartRateDao: HeartRateDao

    companion object {
        const val DATABASE_NAME = "note_database"
    }
}
