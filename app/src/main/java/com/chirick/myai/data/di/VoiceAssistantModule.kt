package com.chirick.myai.data.di

import com.chirick.myai.data.llm.LLMServiceManager
import com.chirick.myai.data.stt.SpeechToTextManager
import com.chirick.myai.data.voiceassistant.VoiceAssistant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoiceAssistantModule {

    @Provides
    @Singleton
    fun provideAudioServiceManager(): SpeechToTextManager {
        return SpeechToTextManager()
    }

    @Provides
    @Singleton
    fun provideLLMServiceManager(): LLMServiceManager {
        return LLMServiceManager()
    }

    @Singleton
    @Provides
    fun provideVoiceAssistant(
        sstManager: SpeechToTextManager,
        llmManager: LLMServiceManager
    ): VoiceAssistant {
        return VoiceAssistant(sstManager, llmManager)
    }
}