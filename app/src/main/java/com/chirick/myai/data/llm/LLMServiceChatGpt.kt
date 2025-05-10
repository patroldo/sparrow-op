package com.chirick.myai.data.llm

import android.util.Log
import com.chirick.chatgpt.ChatGptApi
import com.chirick.myai.BuildConfig

class LLMServiceChatGpt : LLMService {

    private lateinit var chatGpt: ChatGptApi

    override fun init() {
        chatGpt =
            ChatGptApi(BuildConfig.apiChatGptKey)
    }

    override fun translate(text: String): String {
        val answer = chatGpt.sendMessage(text).trimIndent()
        Log.i("ChatGPTAdapter", answer)
        return answer.replace("\\n", System.lineSeparator())
    }

    override fun translate(text: String, filePath: String): String {
        val answer = chatGpt.sendMessage(text, filePath).trimIndent()
        Log.i("ChatGPTAdapter", answer)
        return answer.replace("\\n", System.lineSeparator())
    }
}
