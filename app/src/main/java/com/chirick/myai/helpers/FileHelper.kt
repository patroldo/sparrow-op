package com.chirick.myai.helpers

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object FileHelper {

    fun saveFile(byteArray: ByteArray?, filePath: String): File {
        val file = File(filePath)
        FileOutputStream(file).use { it.write(byteArray) }
        return file
    }

    fun playWavFile(filePath: String) {
        val file = File(filePath)

        if (file.exists()) {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

    fun saveBitmapIntoFile(bitmap: Bitmap, filePath: String): File {
        val file = File(filePath)
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        return file
    }

    fun removeFile(filePath: String) {
        var file = File(filePath)
        if (!file.delete()) {
            Log.e("FileHelper", "Failed to delete file. Check the logs")
        }

    }
}