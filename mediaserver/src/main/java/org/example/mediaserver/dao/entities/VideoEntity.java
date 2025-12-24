package org.example.mediaserver.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA Entity representing a video in the database
// Uses Lombok annotations to generate getters, setters, and constructors automatically
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity {
    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Basic video information fields
    private String title;
    private String description;
    private String url;
    private int durationSeconds;

    // Many-to-one relationship: multiple videos can belong to one creator
    @ManyToOne
    private CreatorEntity creator;
}