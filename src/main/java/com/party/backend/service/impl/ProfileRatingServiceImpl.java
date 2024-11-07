package com.party.backend.service.impl;

import com.party.backend.dto.ProfileRatingDto;
import com.party.backend.entity.ProfileRating;
import com.party.backend.entity.User;
import com.party.backend.mapper.ProfileRatingMapper;
import com.party.backend.repository.ProfileRatingRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.ProfileRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileRatingServiceImpl implements ProfileRatingService {

    private final ProfileRatingRepository profileRatingRepository;
    private final UserRepository userRepository;
    private final ProfileRatingMapper profileRatingMapper;

    @Autowired
    public ProfileRatingServiceImpl(ProfileRatingRepository profileRatingRepository,
                                    UserRepository userRepository,
                                    ProfileRatingMapper profileRatingMapper) {
        this.profileRatingRepository = profileRatingRepository;
        this.userRepository = userRepository;
        this.profileRatingMapper = profileRatingMapper;
    }


    @Override
    @CachePut(value = "profileRatings", key = "#ratedUserId")
    @Transactional
    public ProfileRatingDto addOrUpdateRating(Long ratedUserId, Long ratingUserId, ProfileRatingDto profileRatingDto) {
        ProfileRating profileRating = profileRatingRepository
                .findByRatedUserIdAndRatingUserId(ratedUserId, ratingUserId)
                .orElse(new ProfileRating());

        User ratedUser = userRepository.findById(ratedUserId)
                .orElseThrow(() -> new RuntimeException("Utilisateur noté non trouvé"));
        User ratingUser = userRepository.findById(ratingUserId)
                .orElseThrow(() -> new RuntimeException("Utilisateur évaluateur non trouvé"));

        profileRating.setRatedUser(ratedUser);
        profileRating.setRatingUser(ratingUser);
        profileRating.setRate(profileRatingDto.getRate());
        profileRating.setComment(profileRatingDto.getComment());

        profileRating = profileRatingRepository.save(profileRating);
        return profileRatingMapper.toDto(profileRating);
    }

    @Override
    @Cacheable(value = "profileRatingAverage", key = "#userId")
    public Optional<Double> getAverageRatingForUser(Long userId) {
        return profileRatingRepository.findAverageRateByRatedUserId(userId);
    }

    @Override
    //@Cacheable(value = "profileRatings", key = "#userId")
    public List<ProfileRatingDto> getAllRatingsForUser(Long userId) {
        return profileRatingRepository.findAllByRatedUserIdWithRatingUser(userId)
                .stream()
                .map(profileRatingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"profileRatings", "profileRatingAverage"}, key = "#ratedUserId")
    @Transactional
    public void deleteRating(Long ratedUserId, Long ratingUserId) {
        profileRatingRepository.deleteProfileRatingByUsers(ratedUserId, ratingUserId);
    }
}
