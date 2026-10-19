
package org.example.test;

import org.example.entity.User;
import org.example.exception.AcounntNotActive;
import org.example.exception.InvalidCredentialsException;
import org.example.exception.UserNotFound;
import org.example.service.AuthService;
import org.example.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldAuthenticateWhenCredentialsAreValid() {
        String email = "user@example.com";
        String password = "password123";
        String encodedPassword = "encoded-password";

        User user = mock(User.class);

        when(userService.findByEmail(email)).thenReturn(user);
        when(user.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(password, encodedPassword))
                .thenReturn(true);
        when(user.isActive()).thenReturn(true);

        User result = authService.Login(email, password);

        assertSame(user, result);
        verify(userService).findByEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(user).isActive();
    }

    @Test
    void login_shouldRejectIncorrectPassword() {
        String email = "user@example.com";
        String password = "wrong-password";
        String encodedPassword = "encoded-password";

        User user = mock(User.class);

        when(userService.findByEmail(email)).thenReturn(user);
        when(user.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(password, encodedPassword))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.Login(email, password)
        );

        verify(passwordEncoder).matches(password, encodedPassword);
        verify(user, never()).isActive();
    }

    @Test
    void login_shouldRejectInactiveUser() {
        String email = "inactive@example.com";
        String password = "password123";
        String encodedPassword = "encoded-password";

        User user = mock(User.class);

        when(userService.findByEmail(email)).thenReturn(user);
        when(user.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(password, encodedPassword))
                .thenReturn(true);
        when(user.isActive()).thenReturn(false);

        assertThrows(
                AcounntNotActive.class,
                () -> authService.Login(email, password)
        );

        verify(passwordEncoder).matches(password, encodedPassword);
        verify(user).isActive();
    }

    @Test
    void login_shouldRejectUnknownEmail() {
        String email = "unknown@example.com";
        String password = "password123";

        when(userService.findByEmail(email))
                .thenThrow(new UserNotFound());

        assertThrows(
                UserNotFound.class,
                () -> authService.Login(email, password)
        );

        verifyNoInteractions(passwordEncoder);
    }
}
