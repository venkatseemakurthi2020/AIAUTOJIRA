package com.example.userstoryaiapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // "As a [who], I want [what], so that [why]."
    @Column(length = 1000) // Increase length for longer descriptions
    private String who;
    @Column(length = 1000)
    private String what;
    @Column(length = 1000)
    private String why;
    private String status = "PENDING_REVIEW"; // PENDING_REVIEW, APPROVED, REJECTED, JIRA_CREATED
    private LocalDateTime generatedDate = LocalDateTime.now();
    private Long approvedByUserId; // Link to a user if approved
    private String jiraTicketId; // Jira ticket ID after creation

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnicalTask> tasks;
}