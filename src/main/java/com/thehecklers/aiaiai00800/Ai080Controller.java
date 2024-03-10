package com.thehecklers.aiaiai00800;

import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Ai080Controller {
    private final AzureOpenAiChatClient chatClient;

    private final List<Message> buffer = new ArrayList<>();

    public Ai080Controller(AzureOpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public ChatResponse generate(@RequestParam(defaultValue = "Tell me a joke") String message,
                                 @RequestParam(required = false) String celebrity) {
        List<Message> promptMessages = new ArrayList<>(buffer);

        // Add the user's message
        promptMessages.add(new ChatMessage(MessageType.USER, message));

        if (null != celebrity) {
            // Add a system message to guide the AI's generation
            var sysTemplate = new SystemPromptTemplate("You respond in the style of {celebrity}.");
            Message sysMessage = sysTemplate.createMessage(Map.of("celebrity", celebrity));
            promptMessages.add(sysMessage);
        }

        ChatResponse response = chatClient.call(new Prompt(promptMessages));
/*
        //Use this in cases where you wish/need to request multiple generations
        response.getResults().forEach(g -> {
            System.out.println("   Message Request: " + message);
            System.out.println("   Celebrity Voice: " + celebrity);
            System.out.println("   Content:");
            System.out.println("   " + g.getOutput().getContent());
            buffer.add(g.getOutput());
        });
*/
        buffer.add(response.getResult().getOutput());
        return response;
    }

    @GetMapping("/template")
    public String generateUsingTemplate(@RequestParam String typeOfRequest, @RequestParam String topicOfRequest) {
        PromptTemplate template = new PromptTemplate("Tell me a {type} about {topic}");
        Prompt prompt = template.create(Map.of("type", typeOfRequest, "topic", topicOfRequest));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

    @GetMapping("/weather")
    public AssistantMessage generateWeather(@RequestParam(defaultValue = "New York City") String location) {
        return chatClient.call(new Prompt(new UserMessage("What is the weather in " + location), AzureOpenAiChatOptions.builder()
                        .withFunction("weatherFunction").build()))
                .getResult()
                .getOutput();
    }

    @GetMapping("/about")
    public String generateAbout(@RequestParam(defaultValue = "What are some recommended activities?") String message, @RequestParam(defaultValue = "Chicago") String location) {
        var assistantMessage = generateWeather(location);

        PromptTemplate template = new PromptTemplate("{message} in {location}?");
        Message userMessage = template.createMessage(Map.of("message", message, "location", location));

        return chatClient.call(new Prompt(List.of(assistantMessage, userMessage)))
                .getResult()
                .getOutput()
                .getContent();
    }
}
