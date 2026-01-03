package com.wedding.service;


import com.wedding.entity.User;
import com.wedding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<?> login(String email, String password, boolean rememberMe) {

        if (!rememberMe) {
            return ResponseEntity
                    .badRequest()
                    .body("Please select Remember Me to login");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() ||
                !passwordEncoder.matches(password, userOpt.get().getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Username & password wrong. Please fill correct username & password!");
        }

        User user = userOpt.get();

        if (rememberMe) {
            user.setRememberMeToken(UUID.randomUUID().toString());
            userRepository.save(user);
        }

        user.setPassword(null); // security
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> forgotPassword(String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Email not found");
        }

        User user = userOpt.get();
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset token generated: " + resetToken);
    }


    public ResponseEntity<?> resetPassword(String token, String newPassword) {

        Optional<User> userOpt = userRepository.findByResetPasswordToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid or expired reset token");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }

    public ResponseEntity<?> loginWithToken(String token) {

        Optional<User> userOpt = userRepository.findByRememberMeToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid remember-me token");
        }

        User user = userOpt.get();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}
