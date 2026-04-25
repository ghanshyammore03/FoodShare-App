package com.example.foodshare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshare.data.model.RequestWithDetails
import com.example.foodshare.data.repository.DonationRepository
import com.example.foodshare.data.repository.FoodRequestRepository
import kotlinx.coroutines.launch

class ManageRequestsViewModel(
    private val foodRequestRepository: FoodRequestRepository,
    private val donationRepository: DonationRepository
) : ViewModel() {

    private val _requests = MutableLiveData<List<RequestWithDetails>>()
    val requests: LiveData<List<RequestWithDetails>> = _requests

    private val _actionState = MutableLiveData<String>()
    val actionState: LiveData<String> = _actionState

    fun loadRequestsForDonor(donorUserId: Long) {
        viewModelScope.launch {
            _requests.postValue(foodRequestRepository.getRequestsForDonor(donorUserId))
        }
    }

    fun acceptRequest(requestId: Long, donationId: Long, donorUserId: Long) {
        viewModelScope.launch {
            foodRequestRepository.updateRequestStatus(requestId, "ACCEPTED")
            foodRequestRepository.rejectOtherRequestsForDonation(donationId, requestId)
            donationRepository.updateDonationStatus(donationId, "ASSIGNED")
            _actionState.postValue("Request accepted successfully.")
            loadRequestsForDonor(donorUserId)
        }
    }

    fun rejectRequest(requestId: Long, donorUserId: Long) {
        viewModelScope.launch {
            foodRequestRepository.updateRequestStatus(requestId, "REJECTED")
            _actionState.postValue("Request rejected.")
            loadRequestsForDonor(donorUserId)
        }
    }
}