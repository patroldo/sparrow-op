package com.chirick.myai.data.llm

import kotlinx.coroutines.flow.Flow

interface LLMService {

    fun init()

    fun translate(text: String): String

}