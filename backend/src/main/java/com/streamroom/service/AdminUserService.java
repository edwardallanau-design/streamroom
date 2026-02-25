package com.streamroom.service;

import com.streamroom.dto.AdminUserDTO;
import com.streamroom.dto.CreateUserAdminRequest;
import com.streamroom.entity.User;
import com.streamroom.enums.Role;
import com.streamroom.exception.ForbiddenException;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminUserService implements IAdminUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AdminUserDTO> getAllUsers(String actorRole) {
        Role actor = Role.valueOf(actorRole);
        List<User> users;
        if (actor == Role.ADMIN) {
            users = userRepository.findAll();
        } else if (actor == Role.MODERATOR) {
            users = userRepository.findByRoleIn(List.of(Role.MODERATOR, Role.CONTENT_CREATOR));
        } else {
            throw new ForbiddenException("Insufficient permissions to list users");
        }
        return users.stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional
    public AdminUserDTO createUser(CreateUserAdminRequest request, String actorRole) {
        Role actor = Role.valueOf(actorRole);
        Role targetRole = Role.valueOf(request.role());

        if (actor == Role.CONTENT_CREATOR) {
            throw new ForbiddenException("Content creators cannot create users");
        }
        if (actor == Role.MODERATOR && targetRole == Role.ADMIN) {
            throw new ForbiddenException("Moderators cannot create admin users");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setDisplayName(request.displayName() != null ? request.displayName() : request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(targetRole);
        user.setIsAdmin(targetRole == Role.ADMIN || targetRole == Role.MODERATOR);

        return toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public AdminUserDTO updateRole(Long targetId, String newRole, String actorRole, Long actorId) {
        Role actor = Role.valueOf(actorRole);
        Role target = Role.valueOf(newRole);

        if (actor == Role.CONTENT_CREATOR) {
            throw new ForbiddenException("Content creators cannot change roles");
        }
        if (actorId != null && actorId.equals(targetId)) {
            throw new ForbiddenException("You cannot change your own role");
        }

        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User", targetId));

        if (actor == Role.ADMIN && user.getRole() == Role.ADMIN) {
            throw new ForbiddenException("Admins cannot change another admin's role");
        }
        if (actor == Role.MODERATOR) {
            if (user.getRole() == Role.MODERATOR || user.getRole() == Role.ADMIN) {
                throw new ForbiddenException("Moderators cannot change the role of admins or fellow moderators");
            }
            if (target == Role.ADMIN) {
                throw new ForbiddenException("Moderators cannot assign the admin role");
            }
        }

        user.setRole(target);
        user.setIsAdmin(target == Role.ADMIN || target == Role.MODERATOR);
        return toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long targetId, String actorRole, Long actorId) {
        Role actor = Role.valueOf(actorRole);

        if (actor == Role.CONTENT_CREATOR) {
            throw new ForbiddenException("Content creators cannot delete users");
        }
        if (actorId != null && actorId.equals(targetId)) {
            throw new ForbiddenException("You cannot delete yourself");
        }

        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User", targetId));

        if (actor == Role.ADMIN && user.getRole() == Role.ADMIN) {
            throw new ForbiddenException("Admins cannot delete other admins");
        }
        if (actor == Role.MODERATOR && (user.getRole() == Role.MODERATOR || user.getRole() == Role.ADMIN)) {
            throw new ForbiddenException("Moderators cannot delete admins or fellow moderators");
        }

        userRepository.deleteById(targetId);
    }

    private AdminUserDTO toDTO(User user) {
        return new AdminUserDTO(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null
        );
    }
}
