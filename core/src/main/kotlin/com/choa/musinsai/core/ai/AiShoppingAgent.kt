package com.choa.musinsai.core.ai

import com.choa.musinsai.core.ai.tool.ProductSearchTool
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Component

@Component
class AiShoppingAgent(
    private val chatClient: ChatClient,
    private val productSearchTool: ProductSearchTool
) {

    fun call(messages: List<Message>): String {
        return chatClient
            .prompt(Prompt(messages))
            .tools(productSearchTool)
            .call()
            .content()!!
    }
}
