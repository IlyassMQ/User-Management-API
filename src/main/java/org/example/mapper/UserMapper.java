package org.example.mapper;

import org.example.dto.UserResponseDto;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserResponseDto toDto(User user){
        UserResponseDto dto = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),user.isActive());
        return dto;
    }
}
