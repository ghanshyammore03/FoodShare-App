package com.example.foodshare.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.model.DonationWithDonor

@Dao
interface DonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DonationEntity): Long

    @Query("UPDATE donations SET status = :status WHERE donation_id = :donationId")
    suspend fun updateDonationStatus(donationId: Long, status: String)

    @Query("SELECT * FROM donations WHERE donation_id = :donationId LIMIT 1")
    suspend fun getDonationById(donationId: Long): DonationEntity?

    @Query("SELECT * FROM donations WHERE donor_user_id = :userId ORDER BY created_at DESC")
    suspend fun getDonationsByUser(userId: Long): List<DonationEntity>

    @Transaction
    @Query("SELECT * FROM donations ORDER BY created_at DESC")
    fun getAllDonationsWithDonor(): LiveData<List<DonationWithDonor>>

    @Transaction
    @Query("SELECT * FROM donations WHERE donation_id = :donationId LIMIT 1")
    suspend fun getDonationWithDonorById(donationId: Long): DonationWithDonor?

    @Transaction
    @Query("SELECT * FROM donations WHERE donor_user_id = :userId ORDER BY created_at DESC")
    suspend fun getDonationsWithDonorByUser(userId: Long): List<DonationWithDonor>
}