package com.capgemini.seetbooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.dto.LoginDto;
import com.capgemini.seetbooking.exception.LoginException;
import com.capgemini.seetbooking.exception.UserNotFoundException;
import com.capgemini.seetbooking.model.User;
import com.capgemini.seetbooking.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User registerAdmin(String email, String password) {
		User admin = User.createAdmin(email, password);
		return userRepository.save(admin);
	}

	public boolean isAdmin(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		return user.map(value -> "ADMIN".equals(value.getRole())).orElse(false);
	}

	public User registerUser(User user) {
		// Perform validation and save user
		return userRepository.save(user);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void loginUser(LoginDto userLoginDto) {

		@SuppressWarnings("unused")
		User user = userRepository.findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword())
				.orElseThrow(() -> new LoginException("Invalid credentials"));
	}

	public String updateProfile(Long userId, User updatedUser) {
		Optional<User> existingUser = userRepository.findById(userId);

		if (existingUser.isPresent()) {
			User userToUpdate = existingUser.get();

			// Update profile details
			userToUpdate.setPassword(updatedUser.getPassword());
			userToUpdate.setFirstName(updatedUser.getFirstName());
			userToUpdate.setLastName(updatedUser.getLastName());

			// Save and return the updated user
			userRepository.save(userToUpdate);
			return "User Details Updated";
		}

		throw new UserNotFoundException("User not found with ID: " + userId);
	}

}
