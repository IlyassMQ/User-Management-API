
package org.example.test;

import org.example.entity.Enum.Role;
import org.example.entity.User;
import org.example.exception.EmailAlreadyExists;
import org.example.exception.UserNotFound;
import org.example.repository.UserRepository;
import org.example.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldSaveUserWithEncodedPassword() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encoded-password";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.createUser(username, email, password);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());
        assertTrue(savedUser.isActive());

        verify(passwordEncoder).encode(password);
    }

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "existing@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(
                EmailAlreadyExists.class,
                () -> userService.createUser(
                        "testuser",
                        email,
                        "password123"
                )
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void getAllTheActiveUsers_shouldReturnActiveUsers() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(userRepository.findAllByActiveTrue())
                .thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllTheActiveUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));

        verify(userRepository).findAllByActiveTrue();
    }

    @Test
    void findByEmail_shouldReturnUserWhenFound() {
        String email = "test@example.com";
        User user = mock(User.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertSame(user, result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_shouldThrowExceptionWhenUserNotFound() {
        String email = "missing@example.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFound.class,
                () -> userService.findByEmail(email)
        );

        verify(userRepository).findByEmail(email);
    }

    @Test
    void findById_shouldReturnUserWhenFound() {
        UUID id = UUID.randomUUID();
        User user = mock(User.class);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        User result = userService.findById(id);

        assertSame(user, result);
        verify(userRepository).findById(id);
    }


    @Test
    void updateUser_shouldUpdateUsernameAndEmail() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(
                "new@example.com", userId))
                .thenReturn(false);

        userService.updateUser(
                userId, "New Username", "new@example.com");

        verify(user).setUsername("New Username");
        verify(user).setEmail("new@example.com");
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldRejectDuplicateEmail() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(
                "existing@example.com", userId))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExists.class,
                () -> userService.updateUser(
                        userId, "New Username", "existing@example.com")
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void desactivateUser_shouldSetActiveToFalse() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.desactivateUser(userId);

        verify(user).setActive(false);
        verify(userRepository).save(user);
    }

    @Test
    void findById_shouldThrowWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFound.class,
                () -> userService.findById(userId)
        );
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

}
