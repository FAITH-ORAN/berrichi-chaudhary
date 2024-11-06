package com.party.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String pseudo;
    private String password;
    private Integer age;
    private String interests;
}