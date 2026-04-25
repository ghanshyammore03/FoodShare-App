package com.example.foodshare.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.local.entity.UserEntity

data class DonationWithDonor(
    @Embedded
    val donation: DonationEntity,

    @Relation(
        parentColumn = "donor_user_id",
        entityColumn = "user_id"
    )
    val donor: UserEntity
)