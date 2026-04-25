package com.example.foodshare.data.repository

import androidx.lifecycle.LiveData
import com.example.foodshare.data.local.dao.DonationDao
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.model.DonationWithDonor

class DonationRepository(
    private val donationDao: DonationDao
) {
    fun getAllDonationsWithDonor(): LiveData<List<DonationWithDonor>> {
        return donationDao.getAllDonationsWithDonor()
    }

    suspend fun insertDonation(donation: DonationEntity): Long {
        return donationDao.insertDonation(donation)
    }

    suspend fun getDonationWithDonorById(donationId: Long): DonationWithDonor? {
        return donationDao.getDonationWithDonorById(donationId)
    }

    suspend fun getDonationsByUser(userId: Long): List<DonationEntity> {
        return donationDao.getDonationsByUser(userId)
    }

    suspend fun updateDonationStatus(donationId: Long, status: String) {
        donationDao.updateDonationStatus(donationId, status)
    }
}