package com.chirick.myai.data.stt

import com.chirick.myai.BuildConfig
import com.chirick.sttelevenlabs.ElevenLabs
import kotlinx.coroutines.flow.Flow
import java.io.File

class SpeechToTextServiceElevenLabs : SpeechToTextService {
    private lateinit var eleventLabs: ElevenLabs

    override fun init() {
        eleventLabs = ElevenLabs(BuildConfig.apiEleventLabsKey)
    }

    override fun translate(filepath: String): String {
        val file = File(filepath)
        return eleventLabs.transcribe(file)
    }
}
