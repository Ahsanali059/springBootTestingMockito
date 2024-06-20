package com.example.sbrestapimockitotesting;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getUsername()) ||
                userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword());
        user.setEmail(userRegistrationDto.getEmail());
        return userRepository.save(user);
    }
}
