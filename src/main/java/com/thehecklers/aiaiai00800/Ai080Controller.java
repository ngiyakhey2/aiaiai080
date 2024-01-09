package com.thehecklers.aiaiai00800;

import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ai080Controller {
    private final AzureOpenAiChatClient chatClient;

    public Ai080Controller(AzureOpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String generate(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatClient.generate(message);
    }
}
