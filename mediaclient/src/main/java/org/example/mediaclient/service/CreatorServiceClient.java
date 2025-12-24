package org.example.mediaclient.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.mediaclient.dto.CreatorDto;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.mapper.CreatorMapper;
import org.example.mediaclient.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.proto.*;

import java.util.List;
import java.util.stream.Collectors;

// gRPC client service for creator-related remote calls
// Handles communication with backend server for creator operations
@Service
public class CreatorServiceClient {

    // gRPC stub for making blocking remote procedure calls
    @GrpcClient("mediaserver")
    CreatorServiceGrpc.CreatorServiceBlockingStub stub;

    // Mappers for data transformation
    @Autowired
    private CreatorMapper creatorMapper;

    @Autowired
    private VideoMapper videoMapper;

    // Fetch creator details from remote server
    public CreatorDto getCreator(String id) {
        // Construct the request with creator ID
        CreatorIdRequest request = CreatorIdRequest.newBuilder()
                .setId(id)
                .build();
        Creator creator = stub.getCreator(request);
        return creatorMapper.fromCreatorProtoToCreatorDto(creator);
    }

    public List<VideoDto> getCreatorVideos(String id) {
        CreatorIdRequest request = CreatorIdRequest.newBuilder()
                .setId(id)
                .build();
        VideoStream videoStream = stub.getCreatorVideos(request);
        return videoStream.getVideosList().stream()
                .map(videoMapper::fromVideoProtoToVideoDto)
                .collect(Collectors.toList());
    }
}