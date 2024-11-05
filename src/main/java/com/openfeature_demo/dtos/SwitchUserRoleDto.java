package com.openfeature_demo.dtos;

import com.openfeature_demo.entities.Role;
import com.openfeature_demo.entities.RoleEnum;

public class SwitchUserRoleDto {
    private String email;
    private RoleEnum role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SwitchUserRoleDto{" +
                "email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
