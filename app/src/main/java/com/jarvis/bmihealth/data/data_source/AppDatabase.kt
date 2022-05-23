package com.jarvis.bmihealth.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jarvis.bmihealth.domain.model.ProfileUser

@Database(
    entities = [ProfileUser::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val profileUserDao: ProfileUserDao

    companion object {
        const val DATABASE_NAME = "note_database"
    }
}