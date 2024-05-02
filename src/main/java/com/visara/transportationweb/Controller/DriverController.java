package com.visara.transportationweb.Controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.visara.transportationweb.Model.DriverModel;
import com.visara.transportationweb.Repo.DriverRepository;

@RestController
@RequestMapping("/api")
public class DriverController {

	@Autowired
	private DriverRepository driverRepository;

	@GetMapping("/checkDriverExists/{mobileNumber}")
	public ResponseEntity<Void> checkDriverExists(@PathVariable String mobileNumber, @RequestParam String deviceToken) {
		DriverModel driver = driverRepository.findBydriverMobile(mobileNumber);
		if (driver != null) {
			driver.setDriverlastLoginDateTime(new Date());
			driver.setDeviceToken(deviceToken); // Set the device token
			driverRepository.save(driver);
			return ResponseEntity.ok().build(); // Driver found, return 200 OK
		} else {
			return ResponseEntity.notFound().build(); // Driver not found, return 404 Not Found
		}
	}

	@PutMapping("/drivers/{mobileNumber}/status/location")
	public ResponseEntity<String> updateDriverStatusLocation(@PathVariable String mobileNumber,
			@RequestParam String status, @RequestParam double latitude, @RequestParam double longitude) {
		if (isValidStatus(status)) {
			// Retrieve the driver by mobile number
			DriverModel driver = driverRepository.findBydriverMobile(mobileNumber);
			if (driver != null) {
				// Update the driver's status and location
				driver.setDriverStatus(status);
				driver.setLatitude(latitude);
				driver.setLongtitude(longitude);
				// Save the updated driver
				driverRepository.save(driver);
				// Construct the response message
				String message = status.equals("ONLINE") ? "Driver is now online" : "Driver is now offline";
				return ResponseEntity.ok().body(message);
			} else {
				// Driver not found
				return ResponseEntity.notFound().build();
			}
		} else {
			// Invalid status
			return ResponseEntity.badRequest().body("Invalid status");
		}
	}

	private boolean isValidStatus(String status) {
		return status.equals("ONLINE") || status.equals("OFFLINE");
	}
	
	@PostMapping("/drivers/{mobileNumber}/location/update")
	public ResponseEntity<String> updateLocationPeriodic(@PathVariable String mobileNumber,
	        @RequestParam double latitude, @RequestParam double longitude) {
	    // Update the driver's location
	    DriverModel driver = driverRepository.findBydriverMobile(mobileNumber);
	    if (driver != null) {
	        driver.setLatitude(latitude);
	        driver.setLongtitude(longitude);
	        driverRepository.save(driver);
	        return ResponseEntity.ok().body("Location updated successfully");
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@PutMapping("/logout/{mobileNumber}")
	public ResponseEntity<String> logoutDriver(@PathVariable String mobileNumber) {

		DriverModel driver = driverRepository.findBydriverMobile(mobileNumber);
		if (driver != null) {
			driver.setDriverStatus("OFFLINE");
			driverRepository.save(driver);
			return ResponseEntity.ok("Driver logged out successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/onlineDrivers")
	public List<DriverModel> getOnlineDrivers() {
		return driverRepository.findBydriverStatus("ONLINE");
	}
}
