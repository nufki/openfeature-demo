package com.openfeature_demo.controllers;

import com.openfeature_demo.dtos.RegisterUserDto;
import com.openfeature_demo.dtos.SwitchUserRoleDto;
import com.openfeature_demo.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.openfeature_demo.services.UserService;

@RequestMapping("/admins")
@RestController
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createAdministrator(@RequestBody RegisterUserDto registerUserDto) {
        User createdAdmin = userService.createAdministrator(registerUserDto);

        return ResponseEntity.ok(createdAdmin);
    }


    @PatchMapping("/switch-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> switchUserRole(@RequestBody SwitchUserRoleDto switchUserRoleDto) {
        User createdAdmin = userService.switchUserRole(switchUserRoleDto);

        return ResponseEntity.ok(createdAdmin);
    }
}