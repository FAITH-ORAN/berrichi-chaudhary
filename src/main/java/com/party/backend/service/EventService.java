package com.party.backend.service;

import com.party.backend.dto.EventDto;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventDto saveEvent(EventDto eventDto);
    Optional<EventDto> getEventById(Long id);

    Optional<EventDto> getEventWithLocation(Long id);

    Optional<EventDto> getEventWithEventType(Long id);

    EventDto updateEvent(Long id, EventDto eventDto);
    List<EventDto> getEventsByCity(String city);

    void deleteEvent(Long id);
}