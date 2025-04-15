package com.chirick.myai.data.stt

import kotlinx.coroutines.flow.Flow

interface SpeechToTextService {

    fun init()

    fun translate(filepath: String): String
}
