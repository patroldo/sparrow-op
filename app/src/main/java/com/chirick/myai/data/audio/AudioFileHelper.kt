package com.chirick.myai.data.audio

import android.media.MediaPlayer
import java.io.File
import java.io.FileOutputStream

object AudioFileHelper {

    fun saveWavFile(byteArray: ByteArray?, filePath: String): File {
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
}