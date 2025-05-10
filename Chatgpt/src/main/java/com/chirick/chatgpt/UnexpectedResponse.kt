package com.chirick.chatgpt

import okhttp3.Response

class UnexpectedResponse(response: Response) : Exception() {
    private val code = response.code
    private val localMessage = response.message
    private val localBody = response.body?.string()

    override fun toString(): String {
        return "UnexpectedResponse(code=${code}, " +
                "message=${localMessage}, " +
                "body=${localBody})"
    }
}