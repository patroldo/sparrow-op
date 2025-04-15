package com.chirick.myai.data.stt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.concurrent.thread

class SpeechToTextServiceDummy : SpeechToTextService {

    private var testI: Int = -1;

    override fun init() {}

    override fun translate(filepath: String): String {
        Thread.sleep(100)
        testI++
//        return "Who are you?(${testI})"
        return "ChatGPT capabilities"
    }
}