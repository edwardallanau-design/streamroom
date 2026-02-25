package com.streamroom.service;

import com.streamroom.dto.AdminUserDTO;
import com.streamroom.dto.CreateUserAdminRequest;

import java.util.List;

public interface IAdminUserService {
    List<AdminUserDTO> getAllUsers(String actorRole);

    AdminUserDTO createUser(CreateUserAdminRequest request, String actorRole);

    AdminUserDTO updateRole(Long targetId, String newRole, String actorRole, Long actorId);

    void deleteUser(Long targetId, String actorRole, Long actorId);
}
