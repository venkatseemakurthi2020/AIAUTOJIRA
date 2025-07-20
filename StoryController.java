package com.example.userstoryaiapp.controller;

import com.example.userstoryaiapp.model.UserStory;
import com.example.userstoryaiapp.repository.UserStoryRepository;
import com.example.userstoryaiapp.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
// @CrossOrigin(origins = "http://localhost:4200") // CORS configured globally in application.properties or here if needed
public class StoryController {

    private final AiService aiService;
    private final UserStoryRepository userStoryRepository;

    @Autowired
    public StoryController(AiService aiService, UserStoryRepository userStoryRepository) {
        this.aiService = aiService;
        this.userStoryRepository = userStoryRepository;
    }

    // DTO for incoming request from frontend
    public record GenerateStoriesRequest(String useCase) {}

    /**
     * Endpoint for Business Users to generate user stories and tasks using AI.
     * @param request Contains the high-level business use case.
     * @return List of generated UserStory objects.
     */
    @PostMapping("/generate")
    public ResponseEntity<List<UserStory>> generateUserStories(@RequestBody GenerateStoriesRequest request) {
        if (request.useCase() == null || request.useCase().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            List<UserStory> generatedStories = aiService.generateUserStoriesAndTasks(request.useCase());
            // Save generated stories and tasks to the database
            List<UserStory> savedStories = userStoryRepository.saveAll(generatedStories);
            return ResponseEntity.ok(savedStories);
        } catch (Exception e) {
            System.err.println("Error generating stories: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to retrieve all generated user stories (for display in grid).
     * In a real app, this would be filtered by user or status.
     * @return List of all UserStory objects.
     */
    @GetMapping
    public ResponseEntity<List<UserStory>> getAllUserStories() {
        List<UserStory> stories = userStoryRepository.findAll();
        return ResponseEntity.ok(stories);
    }

    // TODO: Add endpoints for Scrum Masters/PO for review/approval
    // Example:
    /*
    @PutMapping("/{id}/approve")
    public ResponseEntity<UserStory> approveUserStory(@PathVariable Long id) {
        return userStoryRepository.findById(id).map(story -> {
            story.setStatus("APPROVED");
            // In a real app, set approvedByUserId from authenticated user
            return ResponseEntity.ok(userStoryRepository.save(story));
        }).orElse(ResponseEntity.notFound().build());
    }
    */

    // TODO: Add endpoint for Jira integration
    // Example:
    /*
    @PostMapping("/{id}/create-jira-ticket")
    public ResponseEntity<UserStory> createJiraTicket(@PathVariable Long id) {
        // Logic to call JiraService to create tickets
        // Update userStory.jiraTicketId and story.status
        return ResponseEntity.ok().build();
    }
    */
}