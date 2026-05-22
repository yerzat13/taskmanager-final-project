package com.taskmanager.service;

import com.taskmanager.dto.request.AbdrassulayevYerzatRegisterRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatUserResponse;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.exception.AbdrassulayevYerzatBadRequestException;
import com.taskmanager.exception.AbdrassulayevYerzatResourceNotFoundException;
import com.taskmanager.mapper.AbdrassulayevYerzatUserMapper;
import com.taskmanager.repository.AbdrassulayevYerzatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatUserService {

    private final AbdrassulayevYerzatUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AbdrassulayevYerzatUserMapper userMapper;

    public AbdrassulayevYerzatUserResponse register(AbdrassulayevYerzatRegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AbdrassulayevYerzatBadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AbdrassulayevYerzatBadRequestException("Email already exists");
        }

        AbdrassulayevYerzatUser user = AbdrassulayevYerzatUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(AbdrassulayevYerzatUser.Role.USER)
                .build();

        AbdrassulayevYerzatUser savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        return userMapper.toResponse(savedUser);
    }

    public AbdrassulayevYerzatUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            throw new AbdrassulayevYerzatResourceNotFoundException("User not authenticated");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("User", "username", username));
    }

    public AbdrassulayevYerzatUserResponse getCurrentUserResponse() {
        try {
            AbdrassulayevYerzatUser user = getCurrentUser();
            return userMapper.toResponse(user);
        } catch (Exception e) {
            log.warn("Could not get current user: {}", e.getMessage());
            throw new AbdrassulayevYerzatResourceNotFoundException("User not authenticated");
        }
    }

    public AbdrassulayevYerzatUser findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("User", "id", id));
    }
}