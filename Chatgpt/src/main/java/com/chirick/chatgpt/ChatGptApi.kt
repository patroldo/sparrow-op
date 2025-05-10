package com.chirick.chatgpt

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class ChatGptApi(private val apiKey: String) {

    private val apiUrl = "https://api.openai.com/v1/chat/completions"

    private val client = OkHttpClient()

    fun sendMessage(message: String): String {
        val mediaType = "application/json; charset=utf-8".toMediaType()

        val requestBodyText = """
                {
                    "model": "gpt-4o-mini",
                    "messages": [{"role": "user", "content": "$message"}]
                }
            """.trimIndent()

        val body = requestBodyText.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
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

    fun sendMessage(message: String, filePath: String): String {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val base64Image: String = encodeImageIntoBase64(filePath)
        val requestBodyText = """
                {
                    "model": "gpt-4o-mini",
                    "messages": [
                        {
                            "role": "user", 
                            "content": [
                                { "type": "text", "text": "$message" },
                                { "type": "image_url", "image_url": {"url": "data:image/jpeg;base64,${base64Image}"} }
                            ]
                        }
                    ]
                }
            """.trimIndent()

        val body = requestBodyText.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
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

    @OptIn(ExperimentalEncodingApi::class)
    private fun encodeImageIntoBase64(filePath: String): String {
        val bm = BitmapFactory.decodeFile(filePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
        val b = baos.toByteArray()
        return Base64.encode(b)
    }

    fun extractTextFromJson(json: String): String {
        val root = JSONObject(json)
        val choices = root.getJSONArray("choices")
        if (choices.length() > 0) {
            val firstChoice = choices.getJSONObject(0)
            val message = firstChoice.getJSONObject("message")
            return message.optString("content", "")
        } else {
            return ""
        }
    }
}