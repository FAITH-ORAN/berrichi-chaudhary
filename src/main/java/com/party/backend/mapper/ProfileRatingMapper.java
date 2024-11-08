package com.party.backend.mapper;

import com.party.backend.dto.ProfileRatingDto;
import com.party.backend.entity.ProfileRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper(componentModel = "spring")
public interface ProfileRatingMapper {

    ProfileRatingMapper INSTANCE = Mappers.getMapper(ProfileRatingMapper.class);

    @Mapping(source = "ratedUser.id", target = "ratedUserId")
    @Mapping(source = "ratingUser.id", target = "ratingUserId")
    ProfileRatingDto toDto(ProfileRating profileRating);

    @Mapping(source = "ratedUserId", target = "ratedUser.id")
    @Mapping(source = "ratingUserId", target = "ratingUser.id")
    ProfileRating toEntity(ProfileRatingDto profileRatingDto);
}
