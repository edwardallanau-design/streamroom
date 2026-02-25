package com.streamroom.service;

import com.streamroom.dto.ChangePasswordRequest;
import com.streamroom.dto.ProfileDTO;
import com.streamroom.dto.ProfileUpdateRequest;
import com.streamroom.entity.User;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileService implements IProfileService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ProfileDTO getProfile() {
        User admin = userRepository.findFirstByIsAdminTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "isAdmin=true"));
        return toDTO(admin);
    }

    @Override
    @Transactional
    public ProfileDTO updateProfile(ProfileUpdateRequest req) {
        User admin = userRepository.findFirstByIsAdminTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "isAdmin=true"));

        if (req.displayName() != null) admin.setDisplayName(req.displayName());
        if (req.tagline() != null) admin.setTagline(req.tagline());
        if (req.bio() != null) admin.setBio(req.bio());
        if (req.profileImage() != null) admin.setProfileImage(req.profileImage());
        if (req.twitchUsername() != null) admin.setTwitchUsername(req.twitchUsername());
        if (req.schedule() != null) admin.setSchedule(req.schedule());
        if (req.socials() != null) admin.setSocials(req.socials());
        if (req.followerCount() != null) admin.setFollowerCount(req.followerCount());
        if (req.streamCount() != null) admin.setStreamCount(req.streamCount());
        if (req.hoursStreamed() != null) admin.setHoursStreamed(req.hoursStreamed());

        return toDTO(userRepository.save(admin));
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (!passwordEncoder.matches(req.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }

    private ProfileDTO toDTO(User u) {
        return new ProfileDTO(
                u.getDisplayName(),
                u.getTagline(),
                u.getBio(),
                u.getProfileImage(),
                u.getTwitchUsername(),
                u.getSchedule(),
                u.getSocials(),
                u.getFollowerCount(),
                u.getStreamCount(),
                u.getHoursStreamed()
        );
    }
}
