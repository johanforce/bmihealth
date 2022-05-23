package com.jarvis.bmihealth.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileUser(
    val firstname: String,
    val lastname: String,
    val gender: Int,
    val birthday: Long,
    val age: Int,
    val weight: Double,
    val height: Double,
    val bio: String,
    val national: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}