package com.example.userstoryaiapp.service;

import com.example.userstoryaiapp.model.TechnicalTask;
import com.example.userstoryaiapp.model.UserStory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper; // For JSON parsing

    @Autowired
    public AiService(ChatClient chatClient, ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Represents the structured output we expect from the AI.
     */
    public record GeneratedStoriesResponse(
            String highLevelUseCase,
            List<GeneratedUserStory> userStories
    ) {}

    public record GeneratedUserStory(
            String title,
            String who,
            String what,
            String why,
            List<String> technicalTasks
    ) {}


    public List<UserStory> generateUserStoriesAndTasks(String highLevelUseCase) {
        // System message to instruct the AI on its role and desired output format
        String systemPrompt = """
            You are an expert Agile Business Analyst and Software Architect AI.
            Your task is to transform a high-level business use case into well-structured User Stories and their corresponding Technical Tasks.
            
            Each User Story should follow the format: "As a [who], I want [what], so that [why]."
            Each Technical Task should be a concrete, actionable step required to implement the user story.
            
            Provide the output as a JSON object, adhering strictly to the following structure:
            
            {
              "highLevelUseCase": "The original high level use case.",
              "userStories": [
                {
                  "title": "User Story Title (e.g., As a..., I want..., so that...)",
                  "who": "The type of user (e.g., Online Shopper, Administrator, System)",
                  "what": "What the user wants to achieve (e.g., track my order status)",
                  "why": "Why the user wants it (e.g., so that I can know when my package will arrive)",
                  "technicalTasks": [
                    "Task 1 description (e.g., Implement OrderTrackingService for real-time updates)",
                    "Task 2 description (e.g., Design frontend component for order status display)"
                  ]
                },
                // ... more user stories if applicable
              ]
            }
            
            Ensure the JSON is well-formed and complete. Break down the use case into 1-3 user stories if it's complex, each with 3-7 concise technical tasks.
            """;

        UserMessage userMessage = new UserMessage("High-level business use case: " + highLevelUseCase);
        SystemMessage systemMessage = new SystemMessage(systemPrompt);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // Make the call to the LLM
        Generation generation = chatClient.call(prompt).getResult();
        String jsonResponse = generation.getOutput().getContent();

        try {
            // Parse the JSON response into our Java records
            GeneratedStoriesResponse aiResponse = objectMapper.readValue(jsonResponse, GeneratedStoriesResponse.class);

            // Convert the AI-generated records into our JPA entities
            return aiResponse.userStories().stream().map(gs -> {
                UserStory userStory = new UserStory();
                userStory.setTitle(gs.title());
                userStory.setWho(gs.who());
                userStory.setWhat(gs.what());
                userStory.setWhy(gs.why());
                userStory.setGeneratedDate(LocalDateTime.now());
                userStory.setStatus("PENDING_REVIEW"); // Default status

                List<TechnicalTask> tasks = gs.technicalTasks().stream().map(td -> {
                    TechnicalTask task = new TechnicalTask();
                    task.setDescription(td);
                    task.setStatus("TO_DO"); // Default status
                    task.setUserStory(userStory); // Link back to the user story
                    return task;
                }).collect(Collectors.toList());

                userStory.setTasks(tasks);
                return userStory;
            }).collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse AI response JSON: " + jsonResponse);
            throw new RuntimeException("Error processing AI response", e);
        }
    }
}