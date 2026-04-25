package com.example.foodshare.utils

import java.security.MessageDigest

object PasswordUtils {

    fun hashPassword(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray(Charsets.UTF_8))

        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }
}