package com.party.backend.mapper;

import com.party.backend.dto.EventDto;
import com.party.backend.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mappings({
            @Mapping(source = "eventType.id", target = "eventTypeId"),
            @Mapping(source = "organizer.id", target = "organizerId")
    })
    EventDto toDto(Event event);

    @Mappings({
            @Mapping(source = "eventTypeId", target = "eventType.id"),
            @Mapping(source = "organizerId", target = "organizer.id")
    })
    Event toEntity(EventDto eventDto);
}