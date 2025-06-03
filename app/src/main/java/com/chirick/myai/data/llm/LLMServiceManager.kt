package com.chirick.myai.data.llm

import javax.inject.Inject

class LLMServiceManager @Inject constructor() {

    lateinit var current: LLMService
        private set

    init {
        switchService(factory("1"))
    }

    fun switchService(newService: LLMService) {
        current = newService
        current.init()
    }

    private fun factory(str: String): LLMService {
        when (str) {
            "1" -> {
                val currentSvc = LLMServiceDummy()
                currentSvc.init()
                return currentSvc
            }

            "2" -> {
                val currentSvc = LLMServiceChatGpt()
                currentSvc.init()
                return currentSvc
            }

            else -> {
                throw Exception("No such LLM service")
            }
        }
    }
}