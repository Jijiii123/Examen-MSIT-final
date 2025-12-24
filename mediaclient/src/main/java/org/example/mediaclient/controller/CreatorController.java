package org.example.mediaclient.controller;

import org.example.mediaclient.dto.CreatorDto;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.service.CreatorServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST Controller for creator-related HTTP endpoints
// Provides API for retrieving creator information and their videos
@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    // Inject the gRPC client service
    @Autowired
    private CreatorServiceClient creatorService;

    // GET endpoint to retrieve creator by ID
    @GetMapping("/{id}")
    public CreatorDto getCreator(@PathVariable String id) {
        // Delegate to service layer which handles gRPC communication
        return creatorService.getCreator(id);
    }

    // GET endpoint to retrieve all videos created by a specific creator
    @GetMapping("/{id}/videos")
    public List<VideoDto> getCreatorVideos(@PathVariable String id) {
        // Returns list of videos associated with the creator
        return creatorService.getCreatorVideos(id);
    }
}