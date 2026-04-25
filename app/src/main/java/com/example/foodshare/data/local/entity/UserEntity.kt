package com.example.foodshare.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Long = 0L,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password_hash")
    val passwordHash: String,

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "role")
    val role: String = "USER",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)