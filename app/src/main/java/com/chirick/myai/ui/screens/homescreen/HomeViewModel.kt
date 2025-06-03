package com.chirick.myai.ui.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chirick.myai.data.audio.IRecordAudioService
import com.chirick.myai.data.model.ProcessingState
import com.chirick.myai.data.voiceassistant.Resource
import com.chirick.myai.data.voiceassistant.VoiceAssistant
import com.chirick.myai.ui.model.InteractionStep
import com.chirick.myai.ui.model.VoiceAssistantInteractionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val audioService: IRecordAudioService,
    protected val voiceAssistant: VoiceAssistant,
    @Named("recorded_audio") private val filesPath: String
) : ViewModel() {


    val _isSuccess = MutableStateFlow<Boolean>(false)
    val _errorText = MutableStateFlow<String>("")
    protected val _navigationEvent = MutableSharedFlow<Unit>()

    val navigationEvent: SharedFlow<Unit> = _navigationEvent
    val state: StateFlow<VoiceAssistantInteractionModel> = combine(
        audioService.isRecording,
        voiceAssistant.processingState,
        voiceAssistant.translatedText,
        _isSuccess,
        _errorText
    ) { isRecording, processingState, translatedText, isSuccess, errorText ->
        VoiceAssistantInteractionModel(
            translatedText = translatedText,
            isSuccess = isSuccess,
            interactionStep = mapRecordingAndProcessingToInteractionStep(
                processingState,
                isRecording
            ),
            errorText = errorText
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        VoiceAssistantInteractionModel(
            false, "", "", InteractionStep.NOT_PROCESSING,
        )
    )

    private fun mapRecordingAndProcessingToInteractionStep(
        processingState: ProcessingState,
        isRecording: Boolean
    ): InteractionStep {
        return if (processingState != ProcessingState.NOT_PROCESSING) {
            if (processingState == ProcessingState.SPEECH_TO_TEXT) {
                InteractionStep.PROCESSING_ONE
            } else {
                InteractionStep.PROCESSING_TWO
            }
        } else {
            if (isRecording) {
                InteractionStep.LISTENING
            } else {
                InteractionStep.NOT_PROCESSING
            }
        }
    }

    fun startRecording() {
        viewModelScope.launch {
            audioService.prepare(filesPath)
            audioService.start()
        }
    }

    open fun processAudio(): Flow<Resource<Boolean, String>> {
        return voiceAssistant.processAudio(filesPath)
    }

    suspend fun translateVoiceIntoModel() {
        audioService.stop()
        processAudio().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _navigationEvent.emit(Unit) // 👈 Emit navigation signal
                    _isSuccess.value = true
                    _errorText.value = ""
                }

                is Resource.Error -> {
                    _isSuccess.value = false
                    _errorText.value = result.error
                }
            }

        }
    }

    fun startTranslateVoiceIntoModel() {
        viewModelScope.launch(Dispatchers.IO) {
            translateVoiceIntoModel()
        }
    }

    fun resetModel() {
        _isSuccess.value = false
        _errorText.value = ""
    }
}