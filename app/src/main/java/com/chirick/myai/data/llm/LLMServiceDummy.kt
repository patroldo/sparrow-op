package com.chirick.myai.data.llm


class LLMServiceDummy : LLMService {

    private var testI: Int = -1

    private val testDummy =
        "I can assist with a variety of tasks, including but not limited to:\\n\\n1. **Answering Questions**: Providing information on a wide range of topics, from general knowledge to specific inquiries.\\n  \\n2. **Text Generation**: Creating written content such as articles, stories, and essays.\\n\\n3. **Summarization**: Condensing lengthy texts into shorter summaries while retaining key information.\\n\\n4. **Language Translation**: Translating text between different languages.\\n\\n5. **Conversational Engagement**: Engaging in casual conversation on various topics for entertainment or practice.\\n\\n6. **Educational Assistance**: Helping with learning materials, explanations of concepts, and problem-solving in subjects like math, science, and literature.\\n\\n7. **Writing Assistance**: Offering help with grammar, style, and structure in writing projects, including proofreading and editing.\\n\\n8. **Creative Writing**: Assisting with poetry, fiction, and other creative writing formats.\\n\\n9. **Programming Help**: Providing guidance on coding questions and debugging issues.\\n\\n10. **Recommendations**: Suggesting books, movies, music, and other media based on user preferences.\\n\\nIf you have a specific task in mind, feel free to ask!"
    private val testDummy2 = """
        # Hello World
        This is **bold**, and this is _italic_.\n\n- Item 1
        - Item 2
        - Item 3
        
        ```kotlin
        println("Hello Compose!")
        ```
    """

    override fun init() {

    }

    override fun translate(text: String): String {
        Thread.sleep(2000)
        testI++
        return testDummy.trimIndent().replace("\\n", System.lineSeparator())
    }

    override fun translate(text: String, filePath: String): String {
        Thread.sleep(2000)
        testI++
        return testDummy2.trimIndent().replace("\\n", System.lineSeparator())
    }
}