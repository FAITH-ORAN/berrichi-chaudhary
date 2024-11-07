package com.party.backend.dto;

import lombok.Data;

@Data
public class ProfileRatingDto {
    private Long id;
    private Integer rate;
    private String comment;
    private Long ratedUserId;
    private Long ratingUserId;
}
