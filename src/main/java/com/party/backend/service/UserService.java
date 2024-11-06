package com.party.backend.service;

import com.party.backend.dto.UserDto;
import java.util.Optional;

public interface UserService {
    UserDto saveUser(UserDto userCreationDto);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByEmail(String email);
    UserDto updateUser(Long id, UserDto userCreationDto);
    void deleteUser(Long id);
}