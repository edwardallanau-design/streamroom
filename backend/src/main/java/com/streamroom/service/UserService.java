package com.streamroom.service;

import com.streamroom.dto.UserDTO;
import com.streamroom.entity.User;
import com.streamroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user: {}", userDTO.getUsername());
        
        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getUsername() + "@streamroom.local")
                .displayName(userDTO.getDisplayName() != null ? userDTO.getDisplayName() : userDTO.getUsername())
                .bio(userDTO.getBio())
                .profileImage(userDTO.getProfileImage())
                .bannerImage(userDTO.getBannerImage())
                .twitchUsername(userDTO.getTwitchUsername())
                .isAdmin(false)
                .build();

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO getUserByTwitchUsername(String twitchUsername) {
        User user = userRepository.findByTwitchUsername(twitchUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userDTO.getDisplayName() != null) {
            user.setDisplayName(userDTO.getDisplayName());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }
        if (userDTO.getProfileImage() != null) {
            user.setProfileImage(userDTO.getProfileImage());
        }
        if (userDTO.getBannerImage() != null) {
            user.setBannerImage(userDTO.getBannerImage());
        }
        if (userDTO.getTwitchUsername() != null) {
            user.setTwitchUsername(userDTO.getTwitchUsername());
        }

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .bannerImage(user.getBannerImage())
                .twitchUsername(user.getTwitchUsername())
                .build();
    }
}
