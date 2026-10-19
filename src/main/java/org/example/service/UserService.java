package org.example.service;

import org.example.entity.Enum.Role;
import org.example.entity.User;
import org.example.exception.EmailAlreadyExists;
import org.example.exception.InvalidCredentialsException;
import org.example.exception.UserNotFound;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String userName, String email, String password){
        if (userRepository.existsByEmail(email)){
            throw new EmailAlreadyExists();
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(userName,email,hashedPassword,Role.USER,true);

        userRepository.save(user);

    }


    public void updateUser(UUID userId, String newUserName, String newEmail){
        User userToModify = findById(userId);

            if (userRepository.existsByEmailAndIdNot(newEmail,userId)) {
                throw new EmailAlreadyExists();
            }

        userToModify.setUsername(newUserName);
        userToModify.setEmail(newEmail);

        userRepository.save(userToModify);
    }

    public void desactivateUser(UUID userId){
        User userToDesactivate = findById(userId);
        userToDesactivate.setActive(false);

        userRepository.save(userToDesactivate);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }
    public List<User> getAllTheActiveUsers(){
        return userRepository.findAllByActiveTrue();
    }

    public User findById(UUID id){
        Optional<User> userOP =  userRepository.findById(id);
        if (userOP.isEmpty()){
            throw new UserNotFound();
        }
        User user = userOP.get();
        return user;

    }

    public User findByEmail(String email){
        Optional<User> userOP =  userRepository.findByEmail(email);
        if (userOP.isEmpty()){
            throw new UserNotFound();
        }
        User user = userOP.get();
        return user;

    }


}
