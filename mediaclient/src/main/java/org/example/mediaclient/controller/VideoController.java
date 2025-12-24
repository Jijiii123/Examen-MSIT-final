package org.example.mediaclient.controller;

import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.service.VideoServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.proto.Creator;
import org.example.proto.UploadVideoRequest;

// REST Controller for managing video-related HTTP endpoints
// Acts as the gateway between HTTP clients and gRPC backend services
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    // Inject the gRPC client service for backend communication
    @Autowired
    private VideoServiceClient videoService;

    /**
     * Upload a new video to the system
     * Accepts optional query parameters for flexibility in testing
     * If parameters are not provided, default values will be used
     * 
     * @param title Video title (optional)
     * @param description Detailed description of the video content (optional)
     * @param url URL where the video is hosted (optional)
     * @param durationSeconds Length of the video in seconds (optional)
     * @param creatorName Name of the content creator (optional)
     * @param creatorEmail Email address of the creator (optional)
     * @return VideoDto containing the uploaded video details
     */
    @PostMapping("/upload")
    public VideoDto uploadVideo(
            @RequestParam(required = false, defaultValue = "Microservices Tutorial") String title,
            @RequestParam(required = false, defaultValue = "Complete guide to building microservices with gRPC and Spring Boot") String description,
            @RequestParam(required = false, defaultValue = "https://github.com/ameziane/microservices-demo") String url,
            @RequestParam(required = false, defaultValue = "380") Integer durationSeconds,
            @RequestParam(required = false, defaultValue = "AMEZIANE jihane") String creatorName,
            @RequestParam(required = false, defaultValue = "ameziane.jihane@ensam-casa.ma") String creatorEmail
    ) {
        // Step 1: Build the Creator object using Protocol Buffer builder pattern
        Creator creator = Creator.newBuilder()
                .setName(creatorName)
                .setEmail(creatorEmail)
                .build();

        // Step 2: Construct the upload request with all video details
        UploadVideoRequest request = UploadVideoRequest.newBuilder()
                .setTitle(title)
                .setDescription(description)
                .setUrl(url)
                .setDurationSeconds(durationSeconds)
                .setCreator(creator)
                .build();

        // Step 3: Send the request to gRPC backend and receive response
        VideoDto videoDto = videoService.uploadVideo(request);
        // Log the result for debugging purposes
        System.out.println("Video uploaded successfully: " + videoDto);
        return videoDto;
    }

    /**
     * Retrieve video information by its unique identifier
     * 
     * @param id The unique identifier of the video
     * @return VideoDto containing complete video details including creator info
     */
    @GetMapping("/{id}")
    public VideoDto getVideo(@PathVariable String id) {
        // Delegate the request to the gRPC service client
        return videoService.getVideo(id);
    }
}