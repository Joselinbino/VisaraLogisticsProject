package com.visara.transportationweb.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.LoginResponse;
import com.visara.transportationweb.Model.RegisterRequest;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Model.UserModel;
import com.visara.transportationweb.Repo.CfsRepository;
import com.visara.transportationweb.Repo.TransportRepository;
import com.visara.transportationweb.Repo.UserRepository;
import com.visara.transportationweb.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransportRepository transportRepo;

	@Autowired
	private CfsRepository cfsRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void registerUser(RegisterRequest registerRequest) {
		if (registerRequest.getUsername() != null && !registerRequest.getUsername().isEmpty()
				&& registerRequest.getEmail() != null && !registerRequest.getEmail().isEmpty()
				&& registerRequest.getUserType() != null && !registerRequest.getUserType().isEmpty()
				&& registerRequest.getPassword() != null && !registerRequest.getPassword().isEmpty()) {

			UserModel user = new UserModel();

			user.setRegistrationDateTime(new Date());
			user.setLastLoginDateTime(null);
			user.setUserType(registerRequest.getUserType());
			user.setUsername(registerRequest.getUsername());
			user.setEmail(registerRequest.getEmail());

			String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
			user.setPassword(encodedPassword);

			userRepository.save(user);
			// emailService.sendUserEmail(registerRequest.getUsername(),
			// registerRequest.getEmail());
			if ("Transport".equals(registerRequest.getUserType())) {
				TransportsModel transport = new TransportsModel();
				// Populate transport details from registerRequest or UserModel
				transport.setTransportName(registerRequest.getUsername());
				transport.setRegistrationDateTime(new Date());
				transport.setLastLoginDateTime(null);
				transport.setTransportEmail(registerRequest.getEmail());

				// Set other transport attributes as needed
				transportRepo.save(transport);
			} else if ("Container Freight Station".equals(registerRequest.getUserType())) {
				CfsModel cfs = new CfsModel();
				// Populate CFS details from registerRequest or UserModel
				cfs.setCfsName(registerRequest.getUsername());
				cfs.setRegistrationDateTime(new Date());
				cfs.setLastLoginDateTime(null);
				cfs.setCfsemail(registerRequest.getEmail());

				// Set other CFS attributes as needed
				cfsRepo.save(cfs);
			}
		} else {
			throw new IllegalArgumentException("Invalid user data");
		}
	}

	@Override
	public LoginResponse authenticateUser(String email, String password) {
		UserModel user = userRepository.findByemail(email);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			// Password matches (considering you're using password hashing)
			user.setLastLoginDateTime(new Date());
			userRepository.save(user);

			String userType = user.getUserType();

			// Return the role along with the authentication result
			return new LoginResponse(true, userType);
		}

		return new LoginResponse(false, null);
	}

	@Override
	public Optional<UserModel> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserModel getUserById(String userId) {
		return userRepository.findByUserId(userId);
	}

	@Override
	public List<UserModel> getAllUsers() {
		return (List<UserModel>) userRepository.findAll();
	}

	@Override
	public void updateUser(UserModel user) {
		// Handle updating user details, e.g., last login time
		userRepository.save(user);
	}

	public boolean updatePassword(String userId, String newPassword) {
		// Implement your logic to update the password in the database
		// You may need to validate the old password, hash the new password, etc.

		UserModel user = userRepository.findByUserId(userId);

		if (user != null) {
			// Update the user's password
			user.setPassword(newPassword);
			userRepository.save(user);
			return true;
		}

		return false;
	}

	@Override
	public UserModel findByEmail(String email) {

		return userRepository.findByemail(email);
	}

}
