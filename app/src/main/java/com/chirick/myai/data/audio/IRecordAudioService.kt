package com.chirick.myai.data.audio

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class IRecordAudioService {

    private val _isRecording = MutableStateFlow<Boolean>(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    abstract fun prepare(filepath: String)

    fun start() {
        _isRecording.value = true
        _start()
    }

    fun stop() {
        _isRecording.value = false
        _stop()
    }

    abstract protected fun _start()

    abstract protected fun _stop()
}