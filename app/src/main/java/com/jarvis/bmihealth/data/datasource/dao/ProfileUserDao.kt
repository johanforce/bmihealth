package com.jarvis.bmihealth.data.datasource.dao

import androidx.room.*
import com.jarvis.bmihealth.domain.model.entity.ProfileUserEntity

@Dao
interface ProfileUserDao {
    @Query("select * from profileuserentity where id=:id")
    suspend fun getNoteById(id: Int): ProfileUserEntity?

    @Query("select * from profileuserentity")
    suspend fun getAllProfile(): List<ProfileUserEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(profileUser: ProfileUserEntity)

    @Update
    suspend fun updateProfile(profileUser: ProfileUserEntity)

    @Delete
    suspend fun deleteNote(profileUser: ProfileUserEntity)

    @Query("delete from profileuserentity where id = :id")
    suspend fun deleteProfile(id: Int)
}
