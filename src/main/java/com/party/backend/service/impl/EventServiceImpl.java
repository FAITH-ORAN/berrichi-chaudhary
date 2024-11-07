package com.party.backend.service.impl;

import com.party.backend.dto.EventDto;
import com.party.backend.entity.Event;
import com.party.backend.mapper.EventMapper;
import com.party.backend.repository.EventRepository;
import com.party.backend.repository.EventTypeRepository;
import com.party.backend.repository.LocationRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final LocationRepository locationRepository;
    private final EventTypeRepository eventTypeRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper,
                            LocationRepository locationRepository, EventTypeRepository eventTypeRepository,
                            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.locationRepository = locationRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EventDto saveEvent(EventDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);

        eventTypeRepository.findById(eventDto.getEventTypeId())
                .ifPresent(event::setEventType);

        userRepository.findById(eventDto.getOrganizerId())
                .ifPresent(event::setOrganizer);

        // Save the event
        event = eventRepository.save(event);

        // Convert saved entity back to DTO for response
        return eventMapper.toDto(event);
    }

    @Override
    public Optional<EventDto> getEventById(Long id) {
        return eventRepository.findById(id).map(eventMapper::toDto);
    }
}