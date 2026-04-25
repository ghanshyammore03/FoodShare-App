package com.example.foodshare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodshare.data.repository.DonationRepository
import com.example.foodshare.data.repository.FoodRequestRepository
import com.example.foodshare.data.repository.UserRepository

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val donationRepository: DonationRepository,
    private val foodRequestRepository: FoodRequestRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                userRepository,
                donationRepository,
                foodRequestRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}