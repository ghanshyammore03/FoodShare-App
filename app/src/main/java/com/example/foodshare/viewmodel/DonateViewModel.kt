package com.example.foodshare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.repository.DonationRepository
import kotlinx.coroutines.launch

class DonateViewModel(
    private val donationRepository: DonationRepository
) : ViewModel() {

    private val _donateState = MutableLiveData<DonateState>(DonateState.Idle)
    val donateState: LiveData<DonateState> = _donateState

    fun donateFood(
        donorUserId: Long,
        foodName: String,
        quantity: String,
        locationText: String,
        expiryTime: Long,
        imagePath: String?,
        description: String
    ) {
        if (foodName.isBlank() || quantity.isBlank() || locationText.isBlank()) {
            _donateState.value = DonateState.Error("Food name, quantity, and location are required.")
            return
        }

        viewModelScope.launch {
            try {
                donationRepository.insertDonation(
                    DonationEntity(
                        donorUserId = donorUserId,
                        foodName = foodName.trim(),
                        quantity = quantity.trim(),
                        locationText = locationText.trim(),
                        expiryTime = expiryTime,
                        imagePath = imagePath,
                        description = description.trim(),
                        status = "AVAILABLE"
                    )
                )
                _donateState.postValue(DonateState.Success)
            } catch (e: Exception) {
                _donateState.postValue(DonateState.Error(e.message ?: "Failed to donate food."))
            }
        }
    }

    fun resetState() {
        _donateState.value = DonateState.Idle
    }

    sealed class DonateState {
        data object Idle : DonateState()
        data object Success : DonateState()
        data class Error(val message: String) : DonateState()
    }
}