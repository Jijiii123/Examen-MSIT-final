package org.example.mediaserver.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// JPA Entity representing a content creator in the database
// Maintains bidirectional relationship with videos
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorEntity {
    // Auto-generated primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Creator profile information
    private String name;
    private String email;  // Used as unique identifier for duplicate checking

    // One-to-many relationship: one creator can have multiple videos
    // 'mappedBy' indicates this is the inverse side of the relationship
    @OneToMany(mappedBy = "creator")
    private List<VideoEntity> videos;
}