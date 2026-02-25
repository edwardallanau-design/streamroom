package com.streamroom.controller;

import com.streamroom.dto.LoginRequest;
import com.streamroom.dto.LoginResponse;
import com.streamroom.entity.User;
import com.streamroom.repository.UserRepository;
import com.streamroom.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .filter(u -> u.getRole() != null)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(user);
        String role = user.getRole().name();
        return ResponseEntity.ok(new LoginResponse(token, user.getId(), user.getUsername(), user.getDisplayName(), role));
    }
}
