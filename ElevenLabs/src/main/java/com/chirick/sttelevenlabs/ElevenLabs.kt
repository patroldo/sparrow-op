package com.chirick.sttelevenlabs

import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class ElevenLabs(private val apiKey: String) {

    private val client = OkHttpClient()

    fun speak(text: String, onSuccess: (File) -> Unit, onError: (Exception) -> Unit) {
        val url = "https://api.elevenlabs.io/v1/text-to-speech/YOUR_VOICE_ID/stream"

        val jsonBody = """
            {
              "text": "$text",
              "voice_settings": {
                "stability": 0.5,
                "similarity_boost": 0.5
              }
            }
        """.trimIndent()

        val requestBody = jsonBody
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .addHeader("accept", "audio/mpeg")
            .addHeader("xi-api-key", apiKey)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val inputStream = response.body?.byteStream()

            inputStream?.let {
                val tempFile = kotlin.io.path.createTempFile(suffix = ".mp3").toFile()
                tempFile.outputStream().use { fileOut -> inputStream.copyTo(fileOut) }

                onSuccess(tempFile)

//                val mediaPlayer = MediaPlayer()
//                mediaPlayer.setDataSource(tempFile.absolutePath)
//                mediaPlayer.prepare()
//                mediaPlayer.start()
            }
        } else {
            onError(UnexpectedResponse(response))
        }
    }

    fun transcribe(audioFile: File): String {
        val url = "https://api.elevenlabs.io/v1/speech-to-text"

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", audioFile.name,
                audioFile.asRequestBody("audio/wav".toMediaTypeOrNull())
            )
            .addFormDataPart("model_id", "scribe_v1") // <- The model you want
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("xi-api-key", apiKey)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw UnexpectedResponse(response)
        }

        val jsonResponse = response.body?.string()
        if (jsonResponse != null) {
            val text = extractTextFromJson(jsonResponse)
            return text
        } else {
            throw UnexpectedResponse(response)
        }
    }

    private fun extractTextFromJson(json: String): String {
        // You can use a real JSON parser like kotlinx.serialization or org.json
        // For simplicity, here is a VERY basic manual parsing
        val textKey = """"text":""" // search for "text":"..."
        val startIndex = json.indexOf(textKey)
        if (startIndex != -1) {
            val endIndex = json.indexOf('"', startIndex + textKey.length + 1)
            return json.substring(startIndex + textKey.length + 1, endIndex)
        }
        return ""
    }
}