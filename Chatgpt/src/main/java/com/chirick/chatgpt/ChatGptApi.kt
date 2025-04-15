package com.chirick.chatgpt

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

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

    private fun extractTextFromJson(json: String): String {
        // Very simple manual parsing (NOT recommended for production, but okay for quick projects)
        val contentKey = """"content":"""
        val startIndex = json.indexOf(contentKey)
        if (startIndex != -1) {
            val startQuote = json.indexOf('"', startIndex + contentKey.length)
            val endQuote = json.indexOf('"', startQuote + 1)
            if (startQuote != -1 && endQuote != -1) {
                return json.substring(startQuote + 1, endQuote)
            }
        }
        return ""
    }
}