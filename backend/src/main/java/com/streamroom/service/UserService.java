package com.streamroom.service;

import com.streamroom.dto.UserDTO;
import com.streamroom.entity.User;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.mapper.DtoMapper;
import com.streamroom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final DtoMapper mapper;

    public UserService(UserRepository userRepository, DtoMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user '{}'", userDTO.username());

        var user = new User();
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.username() + "@streamroom.local");
        user.setDisplayName(userDTO.displayName() != null ? userDTO.displayName() : userDTO.username());
        user.setBio(userDTO.bio());
        user.setProfileImage(userDTO.profileImage());
        user.setBannerImage(userDTO.bannerImage());
        user.setTwitchUsername(userDTO.twitchUsername());
        user.setIsAdmin(false);

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

        if (userDTO.displayName() != null) user.setDisplayName(userDTO.displayName());
        if (userDTO.bio() != null) user.setBio(userDTO.bio());
        if (userDTO.profileImage() != null) user.setProfileImage(userDTO.profileImage());
        if (userDTO.bannerImage() != null) user.setBannerImage(userDTO.bannerImage());
        if (userDTO.twitchUsername() != null) user.setTwitchUsername(userDTO.twitchUsername());

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
