package com.openfeature_demo.services;

import com.openfeature_demo.dtos.RegisterUserDto;
import com.openfeature_demo.dtos.SwitchUserRoleDto;
import com.openfeature_demo.entities.Role;
import com.openfeature_demo.entities.RoleEnum;
import com.openfeature_demo.entities.User;
import com.openfeature_demo.repositories.RoleRepository;
import com.openfeature_demo.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openfeature_demo.entities.RoleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        return users;
    }

    public User switchUserRole(SwitchUserRoleDto switchUserRoleDto) {
        // Attempt to find the user by email
        Optional<User> user = userRepository.findByEmail(switchUserRoleDto.getEmail());

        // Attempt to find the role based on the role in the DTO
        Optional<Role> role = switch (switchUserRoleDto.getRole()) {
            case RoleEnum.EMPLOYEE -> roleRepository.findByName(RoleEnum.EMPLOYEE);
            case RoleEnum.ADMIN -> roleRepository.findByName(RoleEnum.ADMIN);
            default -> roleRepository.findByName(RoleEnum.CLIENT);
        };

        // Check if both user and role are present
        if (user.isPresent() && role.isPresent()) {
            User userToUpdate = user.get();
            userToUpdate.setRole(role.get());
            userToUpdate.incrementTokenVersion();
            return userRepository.save(userToUpdate);
        } else {
            throw new EntityNotFoundException("User or Role not found");
        }
    }

    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        var user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(optionalRole.get());

        return userRepository.save(user);
    }
}
