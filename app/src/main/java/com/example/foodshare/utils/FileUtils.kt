package com.example.foodshare.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object FileUtils {

    fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null
            val imagesDir = File(context.filesDir, "donation_images")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            val fileName = "donation_${UUID.randomUUID()}.jpg"
            val imageFile = File(imagesDir, fileName)

            val outputStream = FileOutputStream(imageFile)
            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.flush()
            outputStream.close()

            imageFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}