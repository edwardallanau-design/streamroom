package com.streamroom.service;

import com.streamroom.dto.ProfileDTO;
import com.streamroom.dto.ProfileUpdateRequest;

public interface IProfileService {
    ProfileDTO getProfile();
    ProfileDTO updateProfile(ProfileUpdateRequest request);
}
