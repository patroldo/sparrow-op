package com.chirick.myai.ui.model

import com.chirick.myai.data.model.ProcessingState

data class HomeModel(
    val isSuccess: Boolean,
    val errorText: String,
    val translatedText: String,
    val isRecording: Boolean,
    val processingState: ProcessingState
)