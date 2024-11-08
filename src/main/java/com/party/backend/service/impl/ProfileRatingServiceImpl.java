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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(() -> new RuntimeException("user nor found"));
        User ratingUser = userRepository.findById(ratingUserId)
                .orElseThrow(() -> new RuntimeException("user not found "));

        profileRating.setRatedUser(ratedUser);
        profileRating.setRatingUser(ratingUser);
        profileRating.setRate(profileRatingDto.getRate());
        profileRating.setComment(profileRatingDto.getComment());

        profileRating = profileRatingRepository.save(profileRating);

        ProfileRatingDto savedDto = profileRatingMapper.toDto(profileRating);
        savedDto.setRatedUserPseudo(ratedUser.getPseudo());
        savedDto.setRatingUserPseudo(ratingUser.getPseudo());

        return savedDto;
    }

    @Override
    @Cacheable(value = "profileRatingAverage", key = "#userId")
    public Optional<Double> getAverageRatingForUser(Long userId) {
        return profileRatingRepository.findAverageRateByRatedUserId(userId);
    }

    @Override
    public Page<ProfileRatingDto> getAllRatingsForUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> ratingsPage = profileRatingRepository.findAllByRatedUserIdWithRatingAndRatedUserPseudo(userId, pageable);

        List<ProfileRatingDto> ratingDtos = ratingsPage.getContent().stream()
                .map(result -> {
                    ProfileRatingDto dto = new ProfileRatingDto();
                    dto.setId((Long) result[0]);
                    dto.setRate((Integer) result[1]);
                    dto.setComment((String) result[2]);
                    dto.setRatedUserId((Long) result[3]);
                    dto.setRatedUserPseudo((String) result[4]);
                    dto.setRatingUserId((Long) result[5]);
                    dto.setRatingUserPseudo((String) result[6]);
                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(ratingDtos, pageable, ratingsPage.getTotalElements());
    }

    @Override
    @CacheEvict(value = {"profileRatings", "profileRatingAverage"}, key = "#ratedUserId")
    @Transactional
    public void deleteRating(Long ratedUserId, Long ratingUserId) {
        profileRatingRepository.deleteProfileRatingByUsers(ratedUserId, ratingUserId);
    }
}
