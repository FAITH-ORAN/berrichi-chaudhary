package com.party.backend.service;

import com.party.backend.dto.LocationDto;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    LocationDto saveOrUpdateUserLocation(Long userId, LocationDto locationDto);

    Optional<String> getCityByUserId(Long userId);

    LocationDto saveOrUpdateEventLocation(Long eventId, LocationDto locationDto);

    Optional<LocationDto> getEventLocation(Long eventId);

    List<LocationDto> getAllEventsByCity(String city);

    Optional<String> getCityByEventId(Long eventId);
}
