package com.chirick.myai.data.audio

import javax.inject.Inject

class RecordAudioService @Inject constructor() : IRecordAudioService() {

    private val SAMPLE_RATE = 44100
    private val voiceRecorder: IVoiceRecorder = VoiceRecorder()
    private lateinit var filepath: String

    override fun prepare(filepath: String) {
        this.filepath = filepath
        voiceRecorder.prepare(SAMPLE_RATE, 320)
    }

    override fun _start() {
        voiceRecorder.start()
    }

    override fun _stop() {
        val wavFile = WavFileBuilder()
            .setAudioFormat(WavFileBuilder.PCM_AUDIO_FORMAT)
            .setSampleRate(SAMPLE_RATE)
            .setBitsPerSample(WavFileBuilder.BITS_PER_SAMPLE_16)
            .setNumChannels(WavFileBuilder.CHANNELS_MONO)
            .setSubChunk1Size(WavFileBuilder.SUBCHUNK_1_SIZE_PCM)
            .build(voiceRecorder.stop())

        AudioFileHelper.saveWavFile(wavFile, filepath)
    }

}