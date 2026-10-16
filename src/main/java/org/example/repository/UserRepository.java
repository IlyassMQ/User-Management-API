package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndActiveTrue(UUID id);
    List<User> findAllByActiveTrue();
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);//for the update the user can update and keep the same email
}
