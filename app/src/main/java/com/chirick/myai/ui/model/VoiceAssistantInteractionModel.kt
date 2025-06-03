package com.chirick.myai.ui.model

import com.chirick.myai.data.model.ProcessingState

data class VoiceAssistantInteractionModel(
    val isSuccess: Boolean,
    val errorText: String,
    val translatedText: String,
    val interactionStep: InteractionStep,
)

enum class InteractionStep {
    NOT_PROCESSING,
    LISTENING,
    PROCESSING_ONE,
    PROCESSING_TWO
}
