package com.example.foodshare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodshare.data.repository.DonationRepository

class DonateViewModelFactory(
    private val donationRepository: DonationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DonateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DonateViewModel(donationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}