package com.chirick.myai.data.di

import android.content.Context
import com.chirick.myai.data.audio.IRecordAudioService
import com.chirick.myai.data.audio.RecordAudioService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object PictureModule {

    @Provides
    @Named("image_path")
    fun provideImagePath(@ApplicationContext context: Context): String {
        return context.filesDir.absolutePath + "image.jpg"
    }

}