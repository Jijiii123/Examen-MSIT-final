package org.example.mediaserver.dao.repositories;

import org.example.mediaserver.dao.entities.CreatorEntity;
import org.example.mediaserver.dao.entities.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Spring Data repository for Creator entities with custom query methods
public interface CreatorRepository extends JpaRepository<CreatorEntity, Long> {

    // Custom finder method: Spring Data automatically implements this based on method name
    // Finds a creator by their email address (used to prevent duplicate creators)
    Optional<CreatorEntity> findByEmail(String email);

    // Custom JPQL query to fetch all videos associated with a specific creator
    // Uses @Query annotation for more complex queries
    @Query("SELECT v FROM VideoEntity v WHERE v.creator.id = :creatorId")
    List<VideoEntity> findVideosByCreatorId(@Param("creatorId") Long creatorId);
}