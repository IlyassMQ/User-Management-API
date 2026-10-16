package org.example.dto;

import org.example.entity.Enum.Role;

import java.util.UUID;

public class UserResponseDto {

        private UUID id;
        private String userName;
        private String email;
        private Role role;
        private boolean isActive;


    public UserResponseDto() {
    }

    public UserResponseDto(UUID id, String userName, String email, Role role, boolean isActive) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
