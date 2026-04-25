package com.example.foodshare.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    fun formatDateTime(timeMillis: Long): String {
        return SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date(timeMillis))
    }

    fun formatTimeOnly(timeMillis: Long): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(timeMillis))
    }
}