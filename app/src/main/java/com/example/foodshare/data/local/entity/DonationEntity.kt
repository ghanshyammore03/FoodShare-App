package com.example.foodshare.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "donations",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["donor_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["donor_user_id"])
    ]
)
data class DonationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "donation_id")
    val donationId: Long = 0L,

    @ColumnInfo(name = "donor_user_id")
    val donorUserId: Long,

    @ColumnInfo(name = "food_name")
    val foodName: String,

    @ColumnInfo(name = "quantity")
    val quantity: String,

    @ColumnInfo(name = "location_text")
    val locationText: String,

    @ColumnInfo(name = "expiry_time")
    val expiryTime: Long,

    @ColumnInfo(name = "image_path")
    val imagePath: String? = null,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "status")
    val status: String = "AVAILABLE",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)