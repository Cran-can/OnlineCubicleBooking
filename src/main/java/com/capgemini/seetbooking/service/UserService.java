package com.capgemini.seetbooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.model.User;
import com.capgemini.seetbooking.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Perform validation and save user
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> loginUser(String email, String password) {
        // Validate credentials and return user if valid
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            // Generate and set JWT token (you may use a library like jjwt)
            String token = generateJwtToken(user.get());
            user.get().setToken(token);

            // Save the user with the updated token
            return Optional.of(userRepository.save(user.get()));
        }

        return Optional.empty();
    }
    private String generateJwtToken(User user) {
        // Your JWT token generation logic here
        // You may use a library like jjwt for this purpose
        // For simplicity, let's assume a method generateToken exists
        return user.getEmail();
    }
    
    public User updateProfile(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            
            // Update profile details
            userToUpdate.setPassword(updatedUser.getPassword());
            userToUpdate.setFirstName(updatedUser.getFirstName());
            userToUpdate.setLastName(updatedUser.getLastName());
            // Update other profile details as needed

            // Save and return the updated user
            return userRepository.save(userToUpdate);
        }

        throw new RuntimeException("User not found with ID: " + userId);
    }
    public User registerAdmin(String email, String password) {
        User admin = User.createAdmin(email, password);
        return userRepository.save(admin);
    }
    public boolean isAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(value -> "ADMIN".equals(value.getRole())).orElse(false);
    }
}
