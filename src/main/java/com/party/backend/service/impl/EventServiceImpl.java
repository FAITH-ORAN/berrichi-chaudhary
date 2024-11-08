package com.party.backend.service.impl;

import com.party.backend.dto.EventDto;
import com.party.backend.entity.Event;
import com.party.backend.entity.Location;
import com.party.backend.mapper.EventMapper;
import com.party.backend.repository.EventRepository;
import com.party.backend.repository.EventTypeRepository;
import com.party.backend.repository.LocationRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAllWithDetails()
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getEventsByCity(String city) {
        return eventRepository.findAllByCity(city).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventDto> getEventWithLocation(Long id) {
        return eventRepository.findByIdWithLocation(id).map(eventMapper::toDto);
    }

    @Override
    public Optional<EventDto> getEventWithEventType(Long id) {
        return eventRepository.findByIdWithEventType(id).map(eventMapper::toDto);
    }

    @Override
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        event.setEventName(eventDto.getEventName());
        event.setEventDateTime(eventDto.getEventDateTime());
        event.setIsPaid(eventDto.getIsPaid());
        event.setPrice(eventDto.getPrice());
        event.setAvailableSeats(eventDto.getAvailableSeats());

        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    @Override
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        // Manually delete associated Location if present
        if (event.getLocation() != null) {
            Location location = event.getLocation();
            location.setEvent(null); // Break bi-directional relationship
            locationRepository.delete(location); // Delete Location explicitly
        }

        eventRepository.delete(event);
    }
}