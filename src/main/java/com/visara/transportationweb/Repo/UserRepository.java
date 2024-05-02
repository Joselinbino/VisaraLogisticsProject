package com.visara.transportationweb.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

	UserModel findByUserId(String userId);

	Optional<UserModel> findByEmail(String email);

	UserModel findByemail(String email);

}
