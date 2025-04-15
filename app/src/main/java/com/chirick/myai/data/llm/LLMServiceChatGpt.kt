package com.chirick.myai.data.llm

import com.chirick.chatgpt.ChatGptApi
import com.chirick.myai.BuildConfig

class LLMServiceChatGpt : LLMService {

    private lateinit var chatGpt: ChatGptApi

    override fun init() {
        chatGpt =
            ChatGptApi(BuildConfig.apiChatGptKey)
    }

    override fun translate(text: String): String {
        return chatGpt.sendMessage(text).trimIndent().replace("\\n", System.lineSeparator())
    }
}
