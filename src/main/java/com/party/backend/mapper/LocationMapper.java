package com.party.backend.mapper;

import com.party.backend.dto.LocationDto;
import com.party.backend.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(source = "user.id", target = "userId")
   // @Mapping(source = "event.id", target = "eventId")
    LocationDto toDto(Location location);

    @Mapping(source = "userId", target = "user.id")
   // @Mapping(source = "eventId", target = "event.id")
    Location toEntity(LocationDto locationDto);
}