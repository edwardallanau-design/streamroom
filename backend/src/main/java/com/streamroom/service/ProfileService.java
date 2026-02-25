package com.streamroom.service;

import com.streamroom.dto.ProfileDTO;
import com.streamroom.dto.ProfileUpdateRequest;
import com.streamroom.entity.User;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ProfileDTO getProfile() {
        User admin = userRepository.findFirstByIsAdminTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "isAdmin=true"));
        return toDTO(admin);
    }

    @Override
    public ProfileDTO updateProfile(ProfileUpdateRequest req) {
        User admin = userRepository.findFirstByIsAdminTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "isAdmin=true"));

        if (req.displayName() != null) admin.setDisplayName(req.displayName());
        if (req.tagline() != null) admin.setTagline(req.tagline());
        if (req.bio() != null) admin.setBio(req.bio());
        if (req.profileImage() != null) admin.setProfileImage(req.profileImage());
        if (req.twitchUsername() != null) admin.setTwitchUsername(req.twitchUsername());
        if (req.twitchUrl() != null) admin.setTwitchUrl(req.twitchUrl());
        if (req.discordUrl() != null) admin.setDiscordUrl(req.discordUrl());
        if (req.twitterUrl() != null) admin.setTwitterUrl(req.twitterUrl());
        if (req.schedule() != null) admin.setSchedule(req.schedule());
        if (req.followerCount() != null) admin.setFollowerCount(req.followerCount());
        if (req.streamCount() != null) admin.setStreamCount(req.streamCount());
        if (req.hoursStreamed() != null) admin.setHoursStreamed(req.hoursStreamed());

        return toDTO(userRepository.save(admin));
    }

    private ProfileDTO toDTO(User u) {
        return new ProfileDTO(
                u.getDisplayName(),
                u.getTagline(),
                u.getBio(),
                u.getProfileImage(),
                u.getTwitchUsername(),
                u.getTwitchUrl(),
                u.getDiscordUrl(),
                u.getTwitterUrl(),
                u.getSchedule(),
                u.getFollowerCount(),
                u.getStreamCount(),
                u.getHoursStreamed()
        );
    }
}
