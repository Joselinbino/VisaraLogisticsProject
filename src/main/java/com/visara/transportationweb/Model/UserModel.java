package com.visara.transportationweb.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Users")
public class UserModel {
	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// @Column(name = "user_id", unique = true)
	private String userId;

	// Constructor
	public UserModel() {
		// Generate a custom user ID
		this.userId = generateUserId();
	}

	// Method to generate custom user ID
	private String generateUserId() {
		// Logic to generate a unique user ID, for example:
		String prefix = "USER";
		int randomNumber = (int) (Math.random() * 900) + 100; // Generate a random number between 100 and 999
		return prefix + randomNumber;
	}

	@Column
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(unique = true)
	private String email;

	@Column
	private String userType;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDateTime;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDateTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(Date registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}

	public Date getLastLoginDateTime() {
		return lastLoginDateTime;
	}

	public void setLastLoginDateTime(Date lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}

}
