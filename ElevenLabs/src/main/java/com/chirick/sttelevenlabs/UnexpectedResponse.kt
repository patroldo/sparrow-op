package com.chirick.sttelevenlabs

import okhttp3.Response

class UnexpectedResponse constructor(
    private val response: Response
) : Exception() {

    fun getErrorCode(): Int {
        return response.code
    }

    fun getBody(): String {
        return response.body?.string().toString()
    }
}