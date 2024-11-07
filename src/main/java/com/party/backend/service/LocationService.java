package com.party.backend.service;

import com.party.backend.dto.LocationDto;

import java.util.Optional;

public interface LocationService {

    LocationDto saveOrUpdateUserLocation(Long userId, LocationDto locationDto);
    Optional<String> getCityByUserId(Long userId);
     /* Ajouter ou mettre à jour la localisation pour un événement (commenté pour futur usage)
    LocationDto saveOrUpdateEventLocation(Long eventId, LocationDto locationDto);

    // Obtenir la localisation complète (ville et adresse) d'un événement
    Optional<LocationDto> getEventLocation(Long eventId);

    // Lister tous les événements par ville
    List<LocationDto> getAllEventsByCity(String city);
    */

}
