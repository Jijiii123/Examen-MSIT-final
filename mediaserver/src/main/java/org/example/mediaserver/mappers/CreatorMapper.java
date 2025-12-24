package org.example.mediaserver.mappers;

import org.example.mediaserver.dao.entities.CreatorEntity;
import org.springframework.stereotype.Component;
import org.example.proto.Creator;

// Mapper for Creator data transformations between gRPC and database layers
// Ensures data consistency when converting between protobuf messages and JPA entities
@Component
public class CreatorMapper {

    /**
     * Map Protocol Buffer Creator to database entity
     * Extracts creator information from gRPC message
     * 
     * @param creator The protobuf Creator message
     * @return CreatorEntity for database operations
     */
    public CreatorEntity fromProtoToEntity(Creator creator) {
        CreatorEntity entity = new CreatorEntity();
        // Database will auto-generate the primary key (ID)
        // We only map the business fields: name and email
        entity.setName(creator.getName());
        entity.setEmail(creator.getEmail());
        return entity;
    }

    /**
     * Convert JPA entity to Protocol Buffer format
     * Prepares creator data for gRPC response transmission
     * 
     * @param entity The creator entity from database
     * @return Creator protobuf message
     */
    public Creator fromEntityToProto(CreatorEntity entity) {
        // Use protobuf builder to construct the message
        return Creator.newBuilder()
                .setId(String.valueOf(entity.getId()))
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .build();
    }
}