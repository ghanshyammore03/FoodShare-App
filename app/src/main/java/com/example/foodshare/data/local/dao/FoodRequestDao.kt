package com.example.foodshare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodshare.data.local.entity.FoodRequestEntity
import com.example.foodshare.data.model.RequestWithDetails

@Dao
interface FoodRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: FoodRequestEntity): Long

    @Query("UPDATE food_requests SET status = :status WHERE request_id = :requestId")
    suspend fun updateRequestStatus(requestId: Long, status: String)

    @Query("SELECT * FROM food_requests WHERE donation_id = :donationId ORDER BY requested_at DESC")
    suspend fun getRequestsForDonation(donationId: Long): List<FoodRequestEntity>

    @Query("SELECT * FROM food_requests WHERE requester_user_id = :userId ORDER BY requested_at DESC")
    suspend fun getRequestsByUser(userId: Long): List<FoodRequestEntity>

    @Transaction
    @Query("SELECT * FROM food_requests WHERE requester_user_id = :userId ORDER BY requested_at DESC")
    suspend fun getRequestDetailsByUser(userId: Long): List<RequestWithDetails>

    @Transaction
    @Query("SELECT * FROM food_requests ORDER BY requested_at DESC")
    suspend fun getAllRequestDetails(): List<RequestWithDetails>

    @Transaction
    @Query("""
        SELECT * FROM food_requests
        WHERE donation_id IN (
            SELECT donation_id FROM donations WHERE donor_user_id = :donorUserId
        )
        ORDER BY requested_at DESC
    """)
    suspend fun getRequestsForDonor(donorUserId: Long): List<RequestWithDetails>

    @Query("UPDATE food_requests SET status = 'REJECTED' WHERE donation_id = :donationId AND request_id != :acceptedRequestId")
    suspend fun rejectOtherRequestsForDonation(donationId: Long, acceptedRequestId: Long)
}