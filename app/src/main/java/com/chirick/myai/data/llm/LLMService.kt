package com.chirick.myai.data.llm

interface LLMService {

    fun init()

    fun translate(text: String): String
    fun translate(text: String, filePath: String): String

}