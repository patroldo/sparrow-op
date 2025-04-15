package com.chirick.myai.data.voiceassistant

import android.util.Log
import com.chirick.myai.data.llm.LLMServiceManager
import com.chirick.myai.data.model.ProcessingState
import com.chirick.myai.data.stt.SpeechToTextManager
import com.chirick.myai.data.voiceassistant.exceptions.LLMException
import com.chirick.myai.data.voiceassistant.exceptions.STTException
import com.chirick.myai.data.voiceassistant.exceptions.TTSException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VoiceAssistant @Inject constructor(
    private val sttManager: SpeechToTextManager,
    private val llmManager: LLMServiceManager
) {

    private val _translatedText = MutableStateFlow<String>("")
    val translatedText: StateFlow<String> = _translatedText

    private val _answerText = MutableStateFlow<String>("")
    val answerText: StateFlow<String> = _answerText

    private val _processingState = MutableStateFlow<ProcessingState>(ProcessingState.NOT_PROCESSING)
    val processingState: StateFlow<ProcessingState> = _processingState

    fun processAudio(audioFilePath: String): Flow<Resource<Boolean, String>> = flow {
        try {
            _processingState.value = ProcessingState.SPEECH_TO_TEXT
            val text = speechToText(audioFilePath)
            _translatedText.value = text
            _processingState.value = ProcessingState.TEXT_TO_LLM
            val modelAnswer = askLLM(text)
            _answerText.value = modelAnswer
            emit(Resource.Success(true))
        } catch (e: STTException) {
            emit(Resource.Error("(1) Something went wrong.Check logs for details"))
            Log.e("VoiceAssistant_STAGE_1", "Details: ", e)
        } catch (e: LLMException) {
            emit(Resource.Error("(2) Something went wrong.Check logs for details"))
            Log.e("VoiceAssistant_STAGE_2", "Details: ", e)
        } catch (e: TTSException) {
            emit(Resource.Error("(3) Something went wrong.Check logs for details"))
            Log.e("VoiceAssistant_STAGE_3", "Details: ", e)
        } finally {
            _processingState.value = ProcessingState.NOT_PROCESSING
        }
    }

    fun speechToText(audioFilePath: String): String {
        try {
            val text = sttManager.current.translate(audioFilePath)
            return text
        } catch (e: Exception) {
            throw STTException(e)
        }
    }

    fun askLLM(text: String): String {
        try {
            return llmManager.current.translate(text)
        }
        catch (e: Exception) {
            throw LLMException(e)
        }
    }


}