package com.party.backend.service.impl;

import com.party.backend.dto.LocationDto;
import com.party.backend.entity.Location;
import com.party.backend.entity.User;
import com.party.backend.mapper.LocationMapper;
import com.party.backend.repository.LocationRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.locationMapper = locationMapper;
    }
    @Override
    @CachePut(value = "locations", key = "#userId")
    public LocationDto saveOrUpdateUserLocation(Long userId, LocationDto locationDto) {
        Location location = locationRepository.findByUserId(userId)
                .orElse(new Location());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
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

    /* Implémentations futures pour la localisation des événements (commenté pour futur usage)
    @Override
    public LocationDto saveOrUpdateEventLocation(Long eventId, LocationDto locationDto) {
        Location location = locationRepository.findByEventId(eventId)
                .orElse(new Location());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        location.setEvent(event);

        location.setCity(locationDto.getCity());
        location.setAddress(locationDto.getAddress());

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
    */

}
