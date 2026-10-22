package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.dto.LoginRequestDto;
import org.example.entity.User;
import org.example.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request){
        User user = authService.Login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        HttpSession session = request.getSession(true);
        session.setAttribute("id",user.getId());

    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        session.invalidate();
    }



}
