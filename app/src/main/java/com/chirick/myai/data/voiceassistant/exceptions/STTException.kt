package com.chirick.myai.data.voiceassistant.exceptions

class STTException(
    private val e: Exception
) : Exception(e) {

    fun getErrorMessage(): String {
        return "STTException: " + e.message;
    }
}