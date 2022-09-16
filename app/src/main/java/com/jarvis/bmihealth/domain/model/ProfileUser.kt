package com.jarvis.bmihealth.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileUser(
    @ColumnInfo(name = "firstname")
    val firstname: String,
    @ColumnInfo(name = "lastname")
    val lastname: String,
    @ColumnInfo(name = "gender")
    val gender: Int,
    @ColumnInfo(name = "birthday")
    val birthday: Long,
    @ColumnInfo(name = "age")
    val age: Int,
    @ColumnInfo(name = "weight")
    val weight: Double,
    @ColumnInfo(name = "height")
    val height: Double,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var avatar: ByteArray? = null,
    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String,
    @ColumnInfo(name = "bio")
    val bio: String,
    @ColumnInfo(name = "unit")
    val unit: Int,
    @ColumnInfo(name = "national")
    val national: String,
    @ColumnInfo(name = "goal")
    val goal: Int,
    @ColumnInfo(name = "activity_level")
    val activityLevel: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
