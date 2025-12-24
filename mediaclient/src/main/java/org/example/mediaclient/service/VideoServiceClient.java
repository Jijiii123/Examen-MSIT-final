package org.example.mediaclient.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.proto.*;

// Service layer that acts as a gRPC client for video operations
// Communicates with the remote gRPC server and transforms responses to DTOs
@Service
public class VideoServiceClient {

    // Inject the gRPC stub for making remote procedure calls
    // "mediaserver" refers to the server name configured in application.properties
    @GrpcClient("mediaserver")
    VideoServiceGrpc.VideoServiceBlockingStub stub;

    // Inject mapper to convert between protobuf and DTO formats
    @Autowired
    private VideoMapper mapper;

    // Send video upload request to gRPC server and return mapped DTO
    public VideoDto uploadVideo(UploadVideoRequest videoRequest) {
        // Make blocking RPC call to the server
        Video video = stub.uploadVideo(videoRequest);
        // Convert protobuf response to DTO for REST API
        return mapper.fromVideoProtoToVideoDto(video);
    }

    // Retrieve video by ID from gRPC server
    public VideoDto getVideo(String id) {
        // Build the request message
        VideoIdRequest request = VideoIdRequest.newBuilder()
                .setId(id)
                .build();
        // Execute RPC call
        Video video = stub.getVideo(request);
        // Transform to DTO format
        return mapper.fromVideoProtoToVideoDto(video);
    }
}