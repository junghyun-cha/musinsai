package com.choa.musinsai.core.ai

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Component

@Component
class AiShoppingAgent(
    private val chatClient: ChatClient
) {

    fun call(messages: List<Message>): String {
        return chatClient.prompt(Prompt(messages))
            .call().content()!!
    }
}
