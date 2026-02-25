package com.streamroom.controller;

import com.streamroom.dto.ProfileDTO;
import com.streamroom.dto.ProfileUpdateRequest;
import com.streamroom.service.IProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    private final IProfileService profileService;

    public ProfileController(IProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping("/admin/profile")
    public ResponseEntity<ProfileDTO> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }
}
