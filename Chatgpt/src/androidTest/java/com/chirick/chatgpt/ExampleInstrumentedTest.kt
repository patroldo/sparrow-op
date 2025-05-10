package com.chirick.chatgpt

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.chirick.chatgpt.test", appContext.packageName)
    }
    @Test
    fun testJson() {
        val chatGptApi = ChatGptApi("")
        val testResponse = """
            {
              "id": "chatcmpl-BOm51kMsN0XlbftgFvvepvpQR92t9",
              "object": "chat.completion",
              "created": 1745244391,
              "model": "gpt-4o-mini-2024-07-18",
              "choices": [
                {
                  "index": 0,
                  "message": {
                    "role": "assistant",
                    "content": "The cat in the image appears to be white.",
                    "refusal": null,
                    "annotations": []
                  },
                  "logprobs": null,
                  "finish_reason": "stop"
                }
              ],
              "usage": {
                "prompt_tokens": 25513,
                "completion_tokens": 11,
                "total_tokens": 25524,
                "prompt_tokens_details": {
                  "cached_tokens": 0,
                  "audio_tokens": 0
                },
                "completion_tokens_details": {
                  "reasoning_tokens": 0,
                  "audio_tokens": 0,
                  "accepted_prediction_tokens": 0,
                  "rejected_prediction_tokens": 0
                }
              },
              "service_tier": "default",
              "system_fingerprint": "fp_f7d56a8a2c"
            }
        """.trimIndent()
        val actualData = chatGptApi.extractTextFromJson(testResponse)
        val expectedData = "The cat in the image appears to be white."
        assertEquals(expectedData, actualData)
    }
}