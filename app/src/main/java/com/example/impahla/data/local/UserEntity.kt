package com.example.impahla.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.impahla.model.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val dateCreated: Long = System.currentTimeMillis()
)
