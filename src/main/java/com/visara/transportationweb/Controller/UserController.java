package com.visara.transportationweb.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.ForgotPasswordRequest;
import com.visara.transportationweb.Model.LoginRequest;
import com.visara.transportationweb.Model.LoginResponse;
import com.visara.transportationweb.Model.RegisterRequest;
import com.visara.transportationweb.Model.UserModel;
import com.visara.transportationweb.Repo.UserRepository;
import com.visara.transportationweb.Service.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/test")
	public String welcome() {
		return "Test api is sucessful";

	}

	@GetMapping(value = "/username", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getUsername(@RequestParam String userId) {
		UserModel user = userRepository.findByUserId(userId);

		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user.getUsername());
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("User name not found");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
		Optional<UserModel> existingUser = userService.getUserByEmail(registerRequest.getEmail());
		if (existingUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"Email is already registered.\"}");
		}

		try {
			userService.registerUser(registerRequest);
			return ResponseEntity.status(HttpStatus.OK)
					.body("{\"success\": true, \"message\": \"User registered successfully\"}");
		} catch (IllegalArgumentException e) {
			// Handle specific registration exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\": \"Registration failed: " + e.getMessage() + "\"}");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		LoginResponse loginResponse = userService.authenticateUser(email, password);

		if (loginResponse.isSuccess()) {
			return ResponseEntity.ok(loginResponse);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserModel> getUserById(@PathVariable String userId) {
		UserModel user = userService.getUserById(userId);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	@GetMapping("/allUsers")
	public List<UserModel> getAllUsers() {
		return userService.getAllUsers();
	}

	@PutMapping("/update")
	public void updateUser(@RequestBody UserModel user) {
		userService.updateUser(user);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
		// Implement your logic to handle the forgot password request
		// You can validate the old password, set a new password, etc.
		// For simplicity, let's just return a success message

		// Assuming you have a service to handle the password update
		boolean passwordUpdateSuccess = userService.updatePassword(request.getEmail(), request.getNewPassword());

		Map<String, String> response = new HashMap<>();

		if (passwordUpdateSuccess) {
			response.put("message", "Password reset successful");
		} else {
			response.put("message", "Password reset failed. Please try again.");
		}

		return ResponseEntity.ok(response);
	}

}
