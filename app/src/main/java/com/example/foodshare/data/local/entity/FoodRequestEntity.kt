package com.example.foodshare.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_requests",
    foreignKeys = [
        ForeignKey(
            entity = DonationEntity::class,
            parentColumns = ["donation_id"],
            childColumns = ["donation_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["requester_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["donation_id"]),
        Index(value = ["requester_user_id"])
    ]
)
data class FoodRequestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "request_id")
    val requestId: Long = 0L,

    @ColumnInfo(name = "donation_id")
    val donationId: Long,

    @ColumnInfo(name = "requester_user_id")
    val requesterUserId: Long,

    @ColumnInfo(name = "message")
    val message: String = "",

    @ColumnInfo(name = "status")
    val status: String = "PENDING",

    @ColumnInfo(name = "requested_at")
    val requestedAt: Long = System.currentTimeMillis()
)