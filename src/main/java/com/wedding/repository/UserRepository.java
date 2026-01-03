package com.wedding.repository;


import com.wedding.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRememberMeToken(String token);
    Optional<User> findByResetPasswordToken(String token);
}
