package org.example.mediaclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Data Transfer Object for Creator information
// Simple POJO for REST API JSON responses
// Lombok handles all boilerplate code generation
@Data  // Auto-generates getters, setters, and utility methods
@NoArgsConstructor  // Creates empty constructor
@AllArgsConstructor  // Creates constructor with all parameters
public class CreatorDto {
    private String id;
    private String name;
    private String email;
}