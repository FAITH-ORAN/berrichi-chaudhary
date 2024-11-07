package com.party.backend.service;

import com.party.backend.dto.ProfileRatingDto;

import java.util.List;
import java.util.Optional;

public interface ProfileRatingService {

    ProfileRatingDto addOrUpdateRating(Long ratedUserId, Long ratingUserId, ProfileRatingDto profileRatingDto);

    Optional<Double> getAverageRatingForUser(Long userId);

    List<ProfileRatingDto> getAllRatingsForUser(Long userId);

    void deleteRating(Long ratedUserId, Long ratingUserId);

}
