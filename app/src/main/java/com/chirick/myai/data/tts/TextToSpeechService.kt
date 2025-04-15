package com.chirick.myai.data.tts

import android.app.Application;

interface TextToSpeechService {
    fun init(application: Application);

    fun playText(message: String);

    fun stop();
}
