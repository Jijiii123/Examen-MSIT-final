package org.example.mediaserver.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.mediaserver.dao.entities.CreatorEntity;
import org.example.mediaserver.dao.entities.VideoEntity;
import org.example.mediaserver.dao.repositories.CreatorRepository;
import org.example.mediaserver.dao.repositories.VideoRepository;
import org.example.mediaserver.mappers.CreatorMapper;
import org.example.mediaserver.mappers.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.proto.*;

import java.util.Optional;

// gRPC Service implementation for Video operations
// Handles video upload and retrieval, including creator management
// Extends the auto-generated VideoServiceGrpc base class from proto definitions
@GrpcService
public class VideoService extends VideoServiceGrpc.VideoServiceImplBase {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CreatorMapper creatorMapper;

    /**
     * Handle video upload requests from clients
     * Implements the uploadVideo RPC method defined in proto file
     * Process: check creator existence -> save/reuse creator -> save video -> return result
     * 
     * @param request The upload request containing video and creator details
     * @param responseObserver Stream observer for sending response back to client
     */
    @Override
    public void uploadVideo(UploadVideoRequest request, StreamObserver<Video> responseObserver) {
        // Convert the protobuf creator to JPA entity
        CreatorEntity creatorEntity = creatorMapper.fromProtoToEntity(request.getCreator());

        // Check database for existing creator using email as unique identifier
        // This prevents duplicate creator entries
        Optional<CreatorEntity> existingCreator = creatorRepository.findByEmail(creatorEntity.getEmail());

        CreatorEntity savedCreator;
        if (existingCreator.isPresent()) {
            // Creator already exists in database, reuse it
            savedCreator = existingCreator.get();
        } else {
            // Save new creator
            savedCreator = creatorRepository.save(creatorEntity);
        }

        // Map video from request
        VideoEntity videoEntity = videoMapper.fromUploadRequestToEntity(request);
        videoEntity.setCreator(savedCreator);

        // Save video
        VideoEntity savedVideo = videoRepository.save(videoEntity);

        // Convert to proto and send response
        Video video = videoMapper.fromEntityToProto(savedVideo);

        responseObserver.onNext(video);
        responseObserver.onCompleted();
    }

    @Override
    public void getVideo(VideoIdRequest request, StreamObserver<Video> responseObserver) {
        Long videoId = Long.parseLong(request.getId());
        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        Video video = videoMapper.fromEntityToProto(videoEntity);

        responseObserver.onNext(video);
        responseObserver.onCompleted();
    }
}