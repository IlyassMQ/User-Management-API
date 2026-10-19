
package org.example.initilizer;

import jakarta.annotation.PostConstruct;
import org.example.entity.Enum.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeAdmin() {
        User admin = new User("Admin","admin@gmail.com", passwordEncoder.encode("admin123"),Role.ADMIN,true);
        userRepository.save(admin);
    }
}
