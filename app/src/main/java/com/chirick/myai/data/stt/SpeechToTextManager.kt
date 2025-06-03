package com.chirick.myai.data.stt

import javax.inject.Inject

class SpeechToTextManager @Inject constructor() {

    lateinit var current: SpeechToTextService
        private set

    init {
        switchService(factory("1"))
    }

    fun switchService(newService: SpeechToTextService) {
        current = newService
        current.init()
    }

    private fun factory(str: String): SpeechToTextService {
        when (str) {

            "1" -> {
                val currentSvc = SpeechToTextServiceDummy()
                currentSvc.init()
                return currentSvc
            }
            "2" -> {
                val currentSvc = SpeechToTextServiceElevenLabs()
                currentSvc.init()
                return currentSvc
            }

            else -> {
                throw Exception("No such Speech to text service")
            }
        }
    }
}