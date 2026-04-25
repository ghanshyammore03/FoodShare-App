package com.example.foodshare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodshare.data.repository.DonationRepository

class HomeViewModelFactory(
    private val donationRepository: DonationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(donationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}