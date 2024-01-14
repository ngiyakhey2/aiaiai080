package com.thehecklers.aiaiai00800;

import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.messages.ChatMessage;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.MessageType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Ai080Controller {
    private final AzureOpenAiChatClient chatClient;

    private final List<Message> buffer = new ArrayList<>();

    public Ai080Controller(AzureOpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public ChatResponse generate(@RequestParam(defaultValue = "Tell me a joke") String message) {
        List<Message> promptMessages = new ArrayList<>();
        promptMessages.addAll(buffer);
        promptMessages.add(new ChatMessage(MessageType.ASSISTANT, message));

        ChatResponse response = chatClient.generate(new Prompt(promptMessages));
        response.getGenerations().forEach(g -> {
            System.out.println(">> Message Type:");
            System.out.println(g.getMessageType());
            System.out.println(">> Content:");
            System.out.println(g.getContent());
            buffer.add(g);
        });
        return response;
    }
}
