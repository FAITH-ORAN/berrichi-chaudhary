package com.party.backend.controller;

import com.party.backend.dto.ProfileRatingDto;
import com.party.backend.service.ProfileRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile-ratings")
public class ProfileRatingController {

    private final ProfileRatingService profileRatingService;

    @Autowired
    public ProfileRatingController(ProfileRatingService profileRatingService) {
        this.profileRatingService = profileRatingService;
    }

    @PostMapping("/{ratedUserId}/by/{ratingUserId}")
    public ResponseEntity<ProfileRatingDto> addOrUpdateRating(
            @PathVariable Long ratedUserId,
            @PathVariable Long ratingUserId,
            @RequestBody ProfileRatingDto profileRatingDto) {

        ProfileRatingDto updatedRating = profileRatingService.addOrUpdateRating(ratedUserId, ratingUserId, profileRatingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @GetMapping("/{userId}/average")
    public ResponseEntity<Double> getAverageRatingForUser(@PathVariable Long userId) {
        Optional<Double> averageRating = profileRatingService.getAverageRatingForUser(userId);
        return averageRating.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/ratings")
    public ResponseEntity<List<ProfileRatingDto>> getAllRatingsForUser(@PathVariable Long userId) {
        List<ProfileRatingDto> ratings = profileRatingService.getAllRatingsForUser(userId);
        return ResponseEntity.ok(ratings);
    }
    @PutMapping("/{ratedUserId}/by/{ratingUserId}")
    public ResponseEntity<ProfileRatingDto> updateRating(
            @PathVariable Long ratedUserId,
            @PathVariable Long ratingUserId,
            @RequestBody ProfileRatingDto profileRatingDto) {

        ProfileRatingDto updatedRating = profileRatingService.addOrUpdateRating(ratedUserId, ratingUserId, profileRatingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{ratedUserId}/by/{ratingUserId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long ratedUserId,
            @PathVariable Long ratingUserId) {

        profileRatingService.deleteRating(ratedUserId, ratingUserId);
        return ResponseEntity.noContent().build();
    }

}
