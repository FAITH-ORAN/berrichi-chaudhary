package com.party.backend.service;

import com.party.backend.dto.EventDto;
import java.util.Optional;

public interface EventService {
    EventDto saveEvent(EventDto eventDto);
    Optional<EventDto> getEventById(Long id);
}