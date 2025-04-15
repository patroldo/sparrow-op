package com.chirick.chatgpt

import okhttp3.Response

class UnexpectedResponse(response: Response) : Exception() {
}