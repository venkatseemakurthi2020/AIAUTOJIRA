package com.example.userstoryaiapp.repository;

import com.example.userstoryaiapp.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    List<UserStory> findByStatus(String status);
    // You can add custom query methods here if needed
}