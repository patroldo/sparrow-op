package com.chirick.myai.data.voiceassistant

sealed class Resource<out S, out E> {
    data class Success<out S>(val data: S) : Resource<S,Nothing>()
    data class Error<out E>(val error:E) : Resource<Nothing,E>()
}