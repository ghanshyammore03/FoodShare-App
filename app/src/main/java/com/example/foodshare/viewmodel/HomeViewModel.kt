package com.example.foodshare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshare.data.model.DonationWithDonor
import com.example.foodshare.data.repository.DonationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val donationRepository: DonationRepository
) : ViewModel() {

    val donations: LiveData<List<DonationWithDonor>> = donationRepository.getAllDonationsWithDonor()

    suspend fun getDonationById(donationId: Long): DonationWithDonor? {
        return withContext(Dispatchers.IO) {
            donationRepository.getDonationWithDonorById(donationId)
        }
    }
}