package com.party.backend.controller;

import com.party.backend.dto.ProfileRatingDto;
import com.party.backend.service.ProfileRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Operation(summary = "Ajouter  une note et un commentaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note / commentaire ajouté "),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/{ratedUserId}/by/{ratingUserId}")
    public ResponseEntity<ProfileRatingDto> addOrUpdateRating(
            @PathVariable Long ratedUserId,
            @PathVariable Long ratingUserId,
            @RequestBody ProfileRatingDto profileRatingDto) {

        ProfileRatingDto updatedRating = profileRatingService.addOrUpdateRating(ratedUserId, ratingUserId, profileRatingDto);
        return ResponseEntity.ok(updatedRating);
    }
    @Operation(summary = "Obtenir la moyenne des notes pour un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moyenne des notes"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{userId}/average")
    public ResponseEntity<Double> getAverageRatingForUser(@PathVariable Long userId) {
        Optional<Double> averageRating = profileRatingService.getAverageRatingForUser(userId);
        return averageRating.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Obtenir toutes les notes pour un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des notes"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{userId}/ratings")
    public ResponseEntity<Page<ProfileRatingDto>> getAllRatingsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProfileRatingDto> ratings = profileRatingService.getAllRatingsForUser(userId, page, size);
        return ResponseEntity.ok(ratings);
    }
    @Operation(summary = "Mettre à jour une note ou/et un commentaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note / commentaire mise à jour "),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PutMapping("/{ratedUserId}/by/{ratingUserId}")
    public ResponseEntity<ProfileRatingDto> updateRating(
            @PathVariable Long ratedUserId,
            @PathVariable Long ratingUserId,
            @RequestBody ProfileRatingDto profileRatingDto) {

        ProfileRatingDto updatedRating = profileRatingService.addOrUpdateRating(ratedUserId, ratingUserId, profileRatingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @Operation(summary = "Supprimer une note et un commentaire pour un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Note supprimée"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @DeleteMapping("/{ratedUserId}/by/{ratingUserId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long ratedUserId,
            @PathVariable Long ratingUserId) {

        profileRatingService.deleteRating(ratedUserId, ratingUserId);
        return ResponseEntity.noContent().build();
    }

}
