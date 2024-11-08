package com.party.backend.controller;

import com.party.backend.dto.LocationDto;
import com.party.backend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<LocationDto> saveOrUpdateUserLocation(
            @PathVariable Long userId,
            @RequestBody LocationDto locationDto) {

        LocationDto updatedLocation = locationService.saveOrUpdateUserLocation(userId, locationDto);
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/user/{userId}/city")
    public ResponseEntity<String> getCityByUserId(@PathVariable Long userId) {
        Optional<String> city = locationService.getCityByUserId(userId);
        return city.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<LocationDto> updateUserLocation(
            @PathVariable Long userId,
            @RequestBody LocationDto locationDto) {

        LocationDto updatedLocation = locationService.saveOrUpdateUserLocation(userId, locationDto);
        return ResponseEntity.ok(updatedLocation);
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<LocationDto> saveOrUpdateEventLocation(
            @PathVariable Long eventId,
            @RequestBody LocationDto locationDto) {

        LocationDto updatedLocation = locationService.saveOrUpdateEventLocation(eventId, locationDto);
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<LocationDto> getEventLocation(@PathVariable Long eventId) {
        Optional<LocationDto> location = locationService.getEventLocation(eventId);
        return location.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/events/city/{city}")
    public ResponseEntity<List<LocationDto>> getAllEventsByCity(@PathVariable String city) {
        List<LocationDto> events = locationService.getAllEventsByCity(city);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/event/{eventId}/city")
    public ResponseEntity<String> getCityByEventId(@PathVariable Long eventId) {
        Optional<String> city = locationService.getCityByEventId(eventId);
        return city.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
