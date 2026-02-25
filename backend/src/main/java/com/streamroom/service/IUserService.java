package com.streamroom.service;

import com.streamroom.dto.UserDTO;

import java.util.List;

public interface IUserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    UserDTO getUserByTwitchUsername(String twitchUsername);
    UserDTO updateUser(Long id, UserDTO userDTO);
    List<UserDTO> getAllUsers();
    void deleteUser(Long id);
}
