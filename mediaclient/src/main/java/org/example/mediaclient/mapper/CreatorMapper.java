package org.example.mediaclient.mapper;

import org.example.mediaclient.dto.CreatorDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.example.proto.Creator;

// Mapper for transforming Creator protobuf messages to DTO format
// Simplifies conversion between gRPC and REST layers
@Component
public class CreatorMapper {
    // Utility for automatic object mapping
    private ModelMapper modelMapper = new ModelMapper();

    // Transform protobuf Creator to CreatorDto
    // Automatically maps matching field names
    public CreatorDto fromCreatorProtoToCreatorDto(Creator creator) {
        return modelMapper.map(creator, CreatorDto.class);
    }
}