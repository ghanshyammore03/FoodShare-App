package com.example.foodshare.data.repository

import com.example.foodshare.data.local.dao.UserDao
import com.example.foodshare.data.local.entity.UserEntity

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun registerUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(userId: Long): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }
}