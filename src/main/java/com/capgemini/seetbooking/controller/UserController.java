package com.capgemini.seetbooking.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.seetbooking.model.User;
import com.capgemini.seetbooking.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return new ResponseEntity<>("Email already registered", HttpStatus.BAD_REQUEST);
        }

        @SuppressWarnings("unused")
		User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
    
    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody User adminUser) {
        // You can also pass email and password in the request body
        // and use them to register the admin user
        try {
            @SuppressWarnings("unused")
			User registeredAdmin = userService.registerAdmin(adminUser.getEmail(), adminUser.getPassword());
            return new ResponseEntity<>("Admin user registered successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginUser) {
        Optional<User> user = userService.loginUser(loginUser.getEmail(), loginUser.getPassword());
        
        if (user.isPresent()) {
        	// Check if the user is an admin
            boolean isAdmin = userService.isAdmin(user.get().getId());
         // Return token or relevant user information along with the role
            return new ResponseEntity<>(Map.of("user", user.get(), "role", isAdmin ? "ADMIN" : "USER"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
    // Update Profile : firstName,lastName
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestBody User updatedUser) {
        try {
            User updatedUserProfile = userService.updateProfile(userId, updatedUser);
            return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
