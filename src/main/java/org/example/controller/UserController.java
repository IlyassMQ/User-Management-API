package org.example.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.dto.UserCreateRequestDto;
import org.example.dto.UserResponseDto;
import org.example.dto.UserUpdateRequestDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    public UserController(UserService userService,UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserCreateRequestDto userCreateRequestDto){
        userService.createUser(userCreateRequestDto.getUserName(), userCreateRequestDto.getEmail(), userCreateRequestDto.getPassword());
    }

    @GetMapping
    public List<UserResponseDto> getAllUser(){
       List<User> users =  userService.getAll();
       return users.stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/search/{email}")
    public UserResponseDto getUserByEmail(@RequestParam("email") String email){
        User user = userService.findByEmail(email);
        UserResponseDto mappedUser = userMapper.toDto(user);
        return mappedUser;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable("id") UUID id){
        User user = userService.findById(id);
        UserResponseDto mappedUser = userMapper.toDto(user);
        return mappedUser;
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") UUID id, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
        userService.updateUser(id,userUpdateRequestDto.getUserName(),userUpdateRequestDto.getEmail());
    }

    @PatchMapping("/{id}/desactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable("id") UUID id) {
        userService.desactivateUser(id);
    }


    @GetMapping("/me")
    public UserResponseDto getProfile(HttpServletRequest request) throws LoginException {
        HttpSession session = request.getSession(false);
        if (session == null){
            throw new LoginException();
        }
        UUID userId = (UUID) session.getAttribute("id");
        User user = userService.findById(userId);
        return userMapper.toDto(user);
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,HttpServletRequest request) throws LoginException {
        HttpSession session = request.getSession(false);
        if (session == null){
            throw new LoginException();
        }
        UUID userId = (UUID) session.getAttribute("id");
        userService.updateUser(userId,userUpdateRequestDto.getUserName(),userUpdateRequestDto.getEmail());

    }
}
