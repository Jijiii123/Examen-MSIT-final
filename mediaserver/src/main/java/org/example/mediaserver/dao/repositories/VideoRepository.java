package org.example.mediaserver.dao.repositories;

import org.example.mediaserver.dao.entities.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA repository for Video entities
// Provides CRUD operations automatically without implementation
// JpaRepository<VideoEntity, Long> means: entity type is VideoEntity, ID type is Long
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {
    // Spring Data will implement basic operations: save, findById, findAll, delete, etc.
}