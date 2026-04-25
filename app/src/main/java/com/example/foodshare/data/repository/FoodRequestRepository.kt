package com.example.foodshare.data.repository

import com.example.foodshare.data.local.dao.FoodRequestDao
import com.example.foodshare.data.local.entity.FoodRequestEntity
import com.example.foodshare.data.model.RequestWithDetails

class FoodRequestRepository(
    private val foodRequestDao: FoodRequestDao
) {
    suspend fun insertRequest(request: FoodRequestEntity): Long {
        return foodRequestDao.insertRequest(request)
    }

    suspend fun updateRequestStatus(requestId: Long, status: String) {
        foodRequestDao.updateRequestStatus(requestId, status)
    }

    suspend fun getRequestsForDonation(donationId: Long): List<FoodRequestEntity> {
        return foodRequestDao.getRequestsForDonation(donationId)
    }

    suspend fun getRequestDetailsByUser(userId: Long): List<RequestWithDetails> {
        return foodRequestDao.getRequestDetailsByUser(userId)
    }

    suspend fun getAllRequestDetails(): List<RequestWithDetails> {
        return foodRequestDao.getAllRequestDetails()
    }

    suspend fun getRequestsForDonor(donorUserId: Long): List<RequestWithDetails> {
        return foodRequestDao.getRequestsForDonor(donorUserId)
    }

    suspend fun rejectOtherRequestsForDonation(donationId: Long, acceptedRequestId: Long) {
        foodRequestDao.rejectOtherRequestsForDonation(donationId, acceptedRequestId)
    }
}