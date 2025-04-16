# Abbreviation
TTS - text to speech

SST - speech to text

LLM - large language model

# Introduction
Decided to write my own "implementation"(if we can say so) of original rabbit r1 android application. Basically idea behind it is to provide same(or almost the same) capabilities as original apk is providing plus configurability. Let me explain:
When there is any voice interaction with LLM is planned - the sequence is following:
1) Translate speech into text
2) Pass text to LLM
3) (Optionally) Text to speech translation

And by configurability i mean to allow selection of each of those steps to use separate models. E.g.
a) Translate speech into text - Eleven Labs
b) Use ChatGPT to proceed the text
c) And then voice the response from ChatGPT using deepgram

# Why?

Basically trying to implement my open source walkie-talkie with AI with making the source code as readable as possible in order to allow you to extend it per your own needs. Example - using the same application, but replace steps of interaction of ChatGPT with interaction with local ollama. Or if more intresting - to interact it with self-running python server which using interacts with trello using langchain. You're hosting your server which accepts text and do something with your tasks(create few, some to move, some delete)

# Usage

1) Clone repository
2) Create(if not created) in root ```local.properties``` file
3) Add into it apiChatGptKey and apiEleventLabsKey. Example
4) Run your application on the device

```
...
apiChatGptKey="sk-proj-aaaaa"
apiEleventLabsKey="sk_aaaa"
...
```

# Currently planned Task list 

- [x] Write the "bone" application
- [x] Write a minimal usable example
- [x] STT - Add interaction with ElevenLabs
- [ ] STT - Add interaction with Deepgram
- [x] LLM - Add interactino with chatGPT
- [x] LLM - Add interactino with ollama
- [ ] TTS - Add interaction with Deepgram
- [ ] TTS - Add interaction with ElevenLabs
- [ ] Add camera  + text interaction
- [ ] Extend README
- [ ] Add possibility to configure application using qr-code
- [ ] After clean launch start with qr-code reader to read configuration
- [ ] Extend README
- [ ] Integrate with github CI

# Links

Youtube playlist to track the progress(small short demos of current state of application) - https://www.youtube.com/playlist?list=PL7lKhwkfj7OKGUDMnX-fJg4hEy3f6kg5r
