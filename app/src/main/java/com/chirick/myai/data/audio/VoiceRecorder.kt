package com.chirick.myai.data.audio

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import java.io.ByteArrayOutputStream

class VoiceRecorder : IVoiceRecorder {

    private lateinit var recorder: AudioRecord
    private lateinit var buffer: ByteArray
    private val record: ByteArrayOutputStream = ByteArrayOutputStream()
    private var start = false

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override fun prepare(sampleRate: Int, frameSize: Int) {
        val minBufferSize =
            AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize
        )
        buffer = ByteArray(frameSize)
    }

    override fun start() {
        if (!start) {
            recorder.startRecording()
            start = true
            Thread {
                while (start) {
                    recorder.read(buffer, 0, buffer.size)
                    record.write(buffer)
                }
            }.start()
        }
    }

    override fun stop() : ByteArray {
        start = false
        recorder.stop()
        val result = record.toByteArray()
        record.reset()
        return result
    }

    override fun release() {
        start = false
        record.reset()
        recorder.release()
    }
}