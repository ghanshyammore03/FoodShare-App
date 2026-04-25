package com.example.foodshare

import android.app.Application
import com.example.foodshare.data.local.AppDatabase
import com.example.foodshare.data.repository.DonationRepository
import com.example.foodshare.data.repository.FoodRequestRepository
import com.example.foodshare.data.repository.UserRepository

class FoodShareApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    val userRepository: UserRepository by lazy {
        UserRepository(database.userDao())
    }

    val donationRepository: DonationRepository by lazy {
        DonationRepository(database.donationDao())
    }

    val foodRequestRepository: FoodRequestRepository by lazy {
        FoodRequestRepository(database.foodRequestDao())
    }
}