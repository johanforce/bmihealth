package com.jarvis.bmihealth.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jarvis.bmihealth.presentation.pref.ThemeMode.Companion.LIGHT

@Entity
data class ProfileUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "firstname")
    val firstname: String = "",
    @ColumnInfo(name = "lastname")
    val lastname: String = "",
    @ColumnInfo(name = "gender")
    val gender: Int = 0,
    @ColumnInfo(name = "birthday")
    val birthday: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "age")
    val age: Int = 0,
    @ColumnInfo(name = "weight")
    val weight: Double = 0.0,
    @ColumnInfo(name = "height")
    val height: Double = 0.0,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var avatar: ByteArray? = null,
    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String = "",
    @ColumnInfo(name = "bio")
    val bio: String = "",
    @ColumnInfo(name = "unit")
    val unit: Int = 0,
    @ColumnInfo(name = "national")
    val national: String = "vn",
    @ColumnInfo(name = "goal")
    val goal: Int = 0,
    @ColumnInfo(name = "activity_level")
    val activityLevel: Int = 0,
    @ColumnInfo(name = "theme_mode")
    val themeMode: Int = LIGHT
)