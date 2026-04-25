package com.example.foodshare.data.local

import com.example.foodshare.data.local.dao.DonationDao
import com.example.foodshare.data.local.dao.FoodRequestDao
import com.example.foodshare.data.local.dao.UserDao
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.local.entity.FoodRequestEntity
import com.example.foodshare.data.local.entity.UserEntity
import com.example.foodshare.utils.PasswordUtils

object DatabaseSeeder {

    suspend fun seedIfRequired(
        userDao: UserDao,
        donationDao: DonationDao,
        foodRequestDao: FoodRequestDao
    ) {
        if (userDao.getUserCount() > 0) return

        val donorId = userDao.insertUser(
            UserEntity(
                fullName = "Rahul Patil",
                email = "rahul@foodshare.com",
                passwordHash = PasswordUtils.hashPassword("123456"),
                phone = "9876543210",
                address = "Kothrud, Pune",
                role = "DONOR"
            )
        )

        val requesterId = userDao.insertUser(
            UserEntity(
                fullName = "Sneha Joshi",
                email = "sneha@foodshare.com",
                passwordHash = PasswordUtils.hashPassword("123456"),
                phone = "9988776655",
                address = "Shivajinagar, Pune",
                role = "RECEIVER"
            )
        )

        val donationOneId = donationDao.insertDonation(
            DonationEntity(
                donorUserId = donorId,
                foodName = "Veg Biryani",
                quantity = "5 packs",
                locationText = "Kothrud, Pune",
                expiryTime = System.currentTimeMillis() + 2 * 60 * 60 * 1000,
                description = "Fresh homemade biryani packed for pickup.",
                status = "AVAILABLE"
            )
        )

        donationDao.insertDonation(
            DonationEntity(
                donorUserId = donorId,
                foodName = "Chapati and Sabzi",
                quantity = "3 meal boxes",
                locationText = "Erandwane, Pune",
                expiryTime = System.currentTimeMillis() + 3 * 60 * 60 * 1000,
                description = "Packed dinner boxes available immediately.",
                status = "AVAILABLE"
            )
        )

        foodRequestDao.insertRequest(
            FoodRequestEntity(
                donationId = donationOneId,
                requesterUserId = requesterId,
                message = "Need food for two people.",
                status = "PENDING"
            )
        )
    }
}