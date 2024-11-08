package com.party.backend.service.impl;

import com.party.backend.dto.EventDto;
import com.party.backend.dto.LocationDto;
import com.party.backend.entity.Event;
import com.party.backend.entity.Location;
import com.party.backend.mapper.EventMapper;
import com.party.backend.repository.EventRepository;
import com.party.backend.repository.EventTypeRepository;
import com.party.backend.repository.LocationRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @CachePut(value = "events", key = "#result.id")
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
    @Cacheable(value = "events", key = "#id")
    public Optional<EventDto> getEventById(Long id) {
        return eventRepository.findById(id).map(eventMapper::toDto);
    }


    @Override
    @Cacheable(value = "eventsList")
    public Page<EventDto> getAllEvents(Pageable pageable) {
        Page<Event> eventsPage = eventRepository.findAllWithDetails(pageable);

        return eventsPage.map(event -> {
            EventDto eventDto = eventMapper.toDto(event);

            // Set LocationDto
            if (event.getLocation() != null) {
                LocationDto locationDto = new LocationDto();
                locationDto.setId(event.getLocation().getId());
                locationDto.setCity(event.getLocation().getCity());
                locationDto.setAddress(event.getLocation().getAddress());
                locationDto.setEventId(event.getId());

                if (event.getLocation().getUser() != null) {
                    locationDto.setUserId(event.getLocation().getUser().getId());
                }

                eventDto.setLocation(locationDto);
            }

            // Set Organizer pseudo
            if (event.getOrganizer() != null) {
                eventDto.setOrganizerPseudo(event.getOrganizer().getPseudo());
            }

            return eventDto;
        });
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
    @CachePut(value = "events", key = "#id")
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
    @Cacheable(value = "eventDetails", key = "#eventId")
    public Optional<EventDto> getEventWithLocationAndOrganizer(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findEventWithLocationAndOrganizer(eventId);
        return eventOpt.map(event -> {
            EventDto eventDto = eventMapper.toDto(event);

            // Set LocationDto
            if (event.getLocation() != null) {
                LocationDto locationDto = new LocationDto();
                locationDto.setCity(event.getLocation().getCity());
                locationDto.setAddress(event.getLocation().getAddress());
                locationDto.setEventId(event.getId()); // Set the event ID explicitly

                if (event.getLocation().getUser() != null) {
                    locationDto.setUserId(event.getLocation().getUser().getId());
                }

                eventDto.setLocation(locationDto);
            }

            // Set Organizer pseudo
            if (event.getOrganizer() != null) {
                eventDto.setOrganizerPseudo(event.getOrganizer().getPseudo());
            }
            return eventDto;
        });
    }

    @Override
    @CacheEvict(value = "events", key = "#id")
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