package com.chirick.myai.ui.screens.camerascreen

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.chirick.myai.data.audio.IRecordAudioService
import com.chirick.myai.data.voiceassistant.Resource
import com.chirick.myai.data.voiceassistant.VoiceAssistant
import com.chirick.myai.helpers.FileHelper
import com.chirick.myai.ui.screens.homescreen.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CameraViewModel @Inject constructor(
    audioService: IRecordAudioService,
    voiceAssistant: VoiceAssistant,
    @Named("recorded_audio") private val filesPath: String,
    @Named("image_path") private val imagePath: String,
) : HomeViewModel(audioService, voiceAssistant, filesPath) {

    private val _savedBitmap = MutableStateFlow<Bitmap?>(null)
    val savedBitmap: StateFlow<Bitmap?> = _savedBitmap

    fun process() {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmapIntoFile()
            translateVoiceIntoModel()
        }
    }

    override fun processAudio(): Flow<Resource<Boolean, String>> {
        return voiceAssistant.processAudio(filesPath, imagePath)
    }

    fun flushBitmap() {
        _savedBitmap.value = null
    }

    fun saveBitmap(bitmap: Bitmap) {
        _savedBitmap.value = bitmap
    }

    fun saveBitmapIntoFile() {
        FileHelper.saveBitmapIntoFile(savedBitmap.value!!, imagePath)
    }
}