package com.visara.transportationweb.Service;

import java.util.List;
import java.util.Optional;

import com.visara.transportationweb.Model.LoginResponse;
import com.visara.transportationweb.Model.RegisterRequest;
import com.visara.transportationweb.Model.UserModel;

public interface UserService {

	Optional<UserModel> getUserByEmail(String email);

	void registerUser(RegisterRequest registerRequest);

	LoginResponse authenticateUser(String email, String password);

	UserModel getUserById(String userId);

	List<UserModel> getAllUsers();

	void updateUser(UserModel user);

	boolean updatePassword(String email, String newPassword);

	UserModel findByEmail(String email);

}
