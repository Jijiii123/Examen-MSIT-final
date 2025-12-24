package org.example.mediaclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Data Transfer Object for Video information
// Used for JSON serialization in REST API responses
// Lombok annotations generate boilerplate code automatically
@Data  // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor  // Generates no-args constructor
@AllArgsConstructor  // Generates constructor with all fields
public class VideoDto {
    private String id;
    private String title;
    private String description;
    private String url;
    private int durationSeconds;
    private CreatorDto creator;  // Nested creator information
}