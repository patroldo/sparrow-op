package com.chirick.myai.data.audio

interface IVoiceRecorder {

    fun prepare(sampleRate: Int, frameSize: Int)

    fun start()

    fun stop(): ByteArray

    fun release()
}