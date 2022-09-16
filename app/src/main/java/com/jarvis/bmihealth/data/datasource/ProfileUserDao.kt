package com.jarvis.bmihealth.data.datasource
import androidx.room.*
import com.jarvis.bmihealth.domain.model.ProfileUser

@Dao
interface ProfileUserDao {
    @Query("select * from profileuser where id=:id")
    suspend fun getNoteById(id:Int): ProfileUser?

    @Query("select * from profileuser")
    suspend fun getAllProfile(): List<ProfileUser>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(profileUser: ProfileUser)

    @Update
    suspend fun updateNote(profileUser: ProfileUser)

    @Delete
    suspend fun deleteNote(profileUser: ProfileUser)

    @Query("delete from profileuser where id = :id")
    suspend fun deleteProfile(id: Int)
}
