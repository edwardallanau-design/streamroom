package com.streamroom.controller;

import com.streamroom.dto.AdminUserDTO;
import com.streamroom.dto.CreateUserAdminRequest;
import com.streamroom.dto.UpdateRoleRequest;
import com.streamroom.service.IAdminUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final IAdminUserService adminUserService;

    public AdminUserController(IAdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        return ResponseEntity.ok(adminUserService.getAllUsers(role));
    }

    @PostMapping
    public ResponseEntity<AdminUserDTO> createUser(
            @Valid @RequestBody CreateUserAdminRequest body,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserService.createUser(body, role));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<AdminUserDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest body,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        Long actorId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(adminUserService.updateRole(id, body.role(), role, actorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        Long actorId = (Long) request.getAttribute("userId");
        adminUserService.deleteUser(id, role, actorId);
        return ResponseEntity.noContent().build();
    }
}
