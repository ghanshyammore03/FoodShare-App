package com.example.foodshare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.model.RequestWithDetails
import com.example.foodshare.data.repository.DonationRepository
import com.example.foodshare.data.repository.FoodRequestRepository
import com.example.foodshare.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val donationRepository: DonationRepository,
    private val foodRequestRepository: FoodRequestRepository
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _userAddress = MutableLiveData<String>()
    val userAddress: LiveData<String> = _userAddress

    private val _userDonations = MutableLiveData<List<DonationEntity>>()
    val userDonations: LiveData<List<DonationEntity>> = _userDonations

    private val _userRequests = MutableLiveData<List<RequestWithDetails>>()
    val userRequests: LiveData<List<RequestWithDetails>> = _userRequests

    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _userName.postValue(user?.fullName ?: "Unknown User")
            _userEmail.postValue(user?.email ?: "")
            _userAddress.postValue(user?.address ?: "")

            _userDonations.postValue(donationRepository.getDonationsByUser(userId))
            _userRequests.postValue(foodRequestRepository.getRequestDetailsByUser(userId))
        }
    }
}