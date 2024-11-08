package com.party.backend.service.impl;

import com.party.backend.dto.LocationDto;
import com.party.backend.entity.Event;
import com.party.backend.entity.Location;
import com.party.backend.entity.User;
import com.party.backend.mapper.LocationMapper;
import com.party.backend.repository.EventRepository;
import com.party.backend.repository.LocationRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, EventRepository eventRepository, UserRepository userRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.locationMapper = locationMapper;
    }
    @Override
    @CachePut(value = "locations", key = "#userId")
    public LocationDto saveOrUpdateUserLocation(Long userId, LocationDto locationDto) {
        Location location = locationRepository.findByUserId(userId)
                .orElse(new Location());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        location.setUser(user);

        location.setCity(locationDto.getCity());

        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    @Cacheable(value = "locations", key = "#userId")
    public Optional<String> getCityByUserId(Long userId) {
        return locationRepository.findCityByUserId(userId);
    }


    @Override
    public LocationDto saveOrUpdateEventLocation(Long eventId, LocationDto locationDto) {
        Location location = locationRepository.findByEventId(eventId)
                .orElse(new Location());

        // Retrieve the event, throwing an error if it doesn't exist
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        location.setEvent(event);

        // Set the city and address from the DTO
        location.setCity(locationDto.getCity());
        location.setAddress(locationDto.getAddress());

        // Set the user (organizer) as the user for this location
        location.setUser(event.getOrganizer());

        // Save the location and return it as a DTO
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public Optional<LocationDto> getEventLocation(Long eventId) {
        return locationRepository.findByEventId(eventId)
                .map(locationMapper::toDto);
    }

    @Override
    public List<LocationDto> getAllEventsByCity(String city) {
        List<Location> locations = locationRepository.findAllEventsByCity(city);
        return locations.stream()
                        .map(locationMapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<String> getCityByEventId(Long eventId) {
        return locationRepository.findCityByEventId(eventId);
    }

}
