package org.example.mediaserver.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.mediaserver.dao.entities.CreatorEntity;
import org.example.mediaserver.dao.entities.VideoEntity;
import org.example.mediaserver.dao.repositories.CreatorRepository;
import org.example.mediaserver.mappers.CreatorMapper;
import org.example.mediaserver.mappers.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.proto.*;

import java.util.List;
import java.util.stream.Collectors;

// gRPC Service for Creator management operations
// Provides RPC methods for retrieving creator information and their associated videos
// Implements the CreatorService interface defined in proto schema
@GrpcService
public class CreatorService extends CreatorServiceGrpc.CreatorServiceImplBase {

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private CreatorMapper creatorMapper;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * Retrieve creator information by unique identifier
     * Implements the getCreator RPC method
     * 
     * @param request Contains the creator ID to search for
     * @param responseObserver Stream to send the creator data back to client
     */
    @Override
    public void getCreator(CreatorIdRequest request, StreamObserver<Creator> responseObserver) {
        // Parse the string ID to Long for database query
        Long creatorId = Long.parseLong(request.getId());
        // Query database for the creator, throw exception if not found
        CreatorEntity creatorEntity = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found with ID: " + creatorId));

        // Convert JPA entity to protobuf message
        Creator creator = creatorMapper.fromEntityToProto(creatorEntity);

        // Send the response back to the client
        responseObserver.onNext(creator);
        // Signal that the RPC is complete
        responseObserver.onCompleted();
    }

    @Override
    public void getCreatorVideos(CreatorIdRequest request, StreamObserver<VideoStream> responseObserver) {
        Long creatorId = Long.parseLong(request.getId());
        List<VideoEntity> videos = creatorRepository.findVideosByCreatorId(creatorId);

        List<Video> videoList = videos.stream()
                .map(videoMapper::fromEntityToProto)
                .collect(Collectors.toList());

        VideoStream videoStream = VideoStream.newBuilder()
                .addAllVideos(videoList)
                .build();

        responseObserver.onNext(videoStream);
        responseObserver.onCompleted();
    }
}