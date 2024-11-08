package com.party.backend.service;

import com.party.backend.dto.ProfileRatingDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProfileRatingService {

    ProfileRatingDto addOrUpdateRating(Long ratedUserId, Long ratingUserId, ProfileRatingDto profileRatingDto);

    Optional<Double> getAverageRatingForUser(Long userId);

    Page<ProfileRatingDto> getAllRatingsForUser(Long userId, int page, int size);

    void deleteRating(Long ratedUserId, Long ratingUserId);

}
