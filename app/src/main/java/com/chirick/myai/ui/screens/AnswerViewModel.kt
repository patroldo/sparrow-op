package com.chirick.myai.ui.screens

import androidx.lifecycle.ViewModel
import com.chirick.myai.data.voiceassistant.VoiceAssistant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val voiceAssistant: VoiceAssistant
) : ViewModel() {

    val answerText: StateFlow<String> = voiceAssistant.answerText
}