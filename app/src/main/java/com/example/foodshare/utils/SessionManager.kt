package com.example.foodshare.utils

import android.content.Context

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLoginSession(userId: Long, userName: String, userEmail: String) {
        preferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USER_NAME, userName)
            .putString(KEY_USER_EMAIL, userEmail)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getLoggedInUserId(): Long {
        return preferences.getLong(KEY_USER_ID, -1L)
    }

    fun getLoggedInUserName(): String {
        return preferences.getString(KEY_USER_NAME, "") ?: ""
    }

    fun getLoggedInUserEmail(): String {
        return preferences.getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun clearSession() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "foodshare_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
    }
}