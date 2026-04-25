package com.example.foodshare.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.local.entity.FoodRequestEntity
import com.example.foodshare.data.local.entity.UserEntity

data class RequestWithDetails(
    @Embedded
    val request: FoodRequestEntity,

    @Relation(
        parentColumn = "donation_id",
        entityColumn = "donation_id"
    )
    val donation: DonationEntity,

    @Relation(
        parentColumn = "requester_user_id",
        entityColumn = "user_id"
    )
    val requester: UserEntity
)