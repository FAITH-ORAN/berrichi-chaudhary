package com.party.backend.dto;

import lombok.Data;
@Data
public class LocationDto {
    private Long id;
    private String city;
    private String address;
    private Long userId;
    private Long eventId;
}