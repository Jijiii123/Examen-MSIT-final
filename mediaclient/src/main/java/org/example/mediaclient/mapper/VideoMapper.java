package org.example.mediaclient.mapper;

import org.example.mediaclient.dto.CreatorDto;
import org.example.mediaclient.dto.VideoDto;
import org.springframework.stereotype.Component;
import org.example.proto.Video;

// Mapper component for converting protobuf Video messages to DTO objects
@Component
public class VideoMapper {

    // Convert protobuf Video to VideoDto for REST responses
    // Manual mapping is required because protobuf getters don't match DTO field names directly
    public VideoDto fromVideoProtoToVideoDto(Video video) {
        VideoDto dto = new VideoDto();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setUrl(video.getUrl());
        dto.setDurationSeconds(video.getDurationSeconds());
        
        // Map nested creator
        if (video.hasCreator()) {
            CreatorDto creatorDto = new CreatorDto();
            creatorDto.setId(video.getCreator().getId());
            creatorDto.setName(video.getCreator().getName());
            creatorDto.setEmail(video.getCreator().getEmail());
            dto.setCreator(creatorDto);
        }
        
        return dto;
    }
}