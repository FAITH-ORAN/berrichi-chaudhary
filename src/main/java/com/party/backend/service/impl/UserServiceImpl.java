package com.party.backend.service.impl;

import com.party.backend.dto.UserDto;
import com.party.backend.entity.User;
import com.party.backend.mapper.UserMapper;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }
    @Override
    @CachePut(value = "users", key = "#result.id")
    public UserDto saveUser(UserDto userDto) {
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPseudo(userDto.getPseudo());
        user.setPassword(hashedPassword);
        user.setAge(userDto.getAge());
        user.setInterests(userDto.getInterests());

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email")
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    @Override
    @CachePut(value = "users", key = "#id")
    public UserDto updateUser(Long id, UserDto userDto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            user.setEmail(userDto.getEmail());
            user.setPseudo(userDto.getPseudo());
            user.setAge(userDto.getAge());
            user.setInterests(userDto.getInterests());

            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(userDto.getPassword());
                user.setPassword(hashedPassword);
            }

            user = userRepository.save(user);
            return userMapper.toDto(user);
        } else {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + id);
        }
    }
    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
