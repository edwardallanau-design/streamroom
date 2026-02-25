package com.streamroom.controller;

import com.streamroom.dto.ChangePasswordRequest;
import com.streamroom.dto.ProfileDTO;
import com.streamroom.dto.ProfileUpdateRequest;
import com.streamroom.service.IProfileService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/admin/profile/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        profileService.changePassword(request, userId);
        return ResponseEntity.noContent().build();
    }
}
