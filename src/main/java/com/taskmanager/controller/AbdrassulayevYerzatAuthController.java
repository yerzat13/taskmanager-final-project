package com.taskmanager.controller;

import com.taskmanager.dto.request.AbdrassulayevYerzatLoginRequest;
import com.taskmanager.dto.request.AbdrassulayevYerzatRegisterRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatAuthResponse;
import com.taskmanager.dto.response.AbdrassulayevYerzatUserResponse;
import com.taskmanager.security.AbdrassulayevYerzatJwtUtil;
import com.taskmanager.service.AbdrassulayevYerzatAuditService;
import com.taskmanager.service.AbdrassulayevYerzatUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AbdrassulayevYerzatAuthController {

    private final AuthenticationManager authenticationManager;
    private final AbdrassulayevYerzatJwtUtil jwtUtil;
    private final AbdrassulayevYerzatUserService userService;
    private final AbdrassulayevYerzatAuditService auditService;

    @PostMapping("/register")
    public ResponseEntity<AbdrassulayevYerzatUserResponse> register(
            @Valid @RequestBody AbdrassulayevYerzatRegisterRequest request,
            HttpServletRequest httpRequest) {

        log.info("Register request for username: {}", request.getUsername());

        AbdrassulayevYerzatUserResponse userResponse = userService.register(request);

        auditService.logAction(
                "REGISTER",
                request.getUsername(),
                "User registered successfully",
                httpRequest.getRemoteAddr()
        );

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AbdrassulayevYerzatAuthResponse> login(
            @Valid @RequestBody AbdrassulayevYerzatLoginRequest request,
            HttpServletRequest httpRequest) {

        log.info("Login request for username: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        String token = jwtUtil.generateToken(request.getUsername(), role);

        auditService.logAction(
                "LOGIN",
                request.getUsername(),
                "User logged in successfully",
                httpRequest.getRemoteAddr()
        );

        return ResponseEntity.ok(AbdrassulayevYerzatAuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(request.getUsername())
                .email("")
                .role(role)
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<AbdrassulayevYerzatUserResponse> getCurrentUser() {
        log.info("Get current user info");
        return ResponseEntity.ok(userService.getCurrentUserResponse());
    }
}