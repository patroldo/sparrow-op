package com.chirick.myai.data.di

import android.content.Context
import com.chirick.myai.data.audio.RecordAudioService
import com.chirick.myai.data.audio.IRecordAudioService
import com.chirick.myai.data.stt.SpeechToTextManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Provides
    fun provideAudioService(): IRecordAudioService {
        return RecordAudioService()
    }

    @Provides
    @Named("recorded_audio")
    fun provideRecordedAudioPath(@ApplicationContext context: Context): String {
        return context.filesDir.absolutePath + "audio.wav"
    }

    @Provides
    @Named("text_to_speech_path")
    fun provideTextToSpeechAudioDirPath(@ApplicationContext context: Context): String {
        return context.filesDir.absolutePath
    }

}