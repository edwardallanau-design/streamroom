package com.streamroom.service;

import com.streamroom.dto.UserDTO;
import com.streamroom.entity.User;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.mapper.DtoMapper;
import com.streamroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final DtoMapper mapper;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user '{}'", userDTO.getUsername());

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

        return mapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        return mapper.toUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return mapper.toUserDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username)));
    }

    @Override
    public UserDTO getUserByTwitchUsername(String twitchUsername) {
        return mapper.toUserDTO(userRepository.findByTwitchUsername(twitchUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", twitchUsername)));
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (userDTO.getDisplayName() != null) user.setDisplayName(userDTO.getDisplayName());
        if (userDTO.getBio() != null) user.setBio(userDTO.getBio());
        if (userDTO.getProfileImage() != null) user.setProfileImage(userDTO.getProfileImage());
        if (userDTO.getBannerImage() != null) user.setBannerImage(userDTO.getBannerImage());
        if (userDTO.getTwitchUsername() != null) user.setTwitchUsername(userDTO.getTwitchUsername());

        return mapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(mapper::toUserDTO).toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user id={}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
