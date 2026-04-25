package com.example.foodshare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodshare.data.repository.DonationRepository
import com.example.foodshare.data.repository.FoodRequestRepository

class ManageRequestsViewModelFactory(
    private val foodRequestRepository: FoodRequestRepository,
    private val donationRepository: DonationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageRequestsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageRequestsViewModel(
                foodRequestRepository,
                donationRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}