package com.visara.transportationweb.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Model.VehiclesModel;
import com.visara.transportationweb.Service.TransportService;
import com.visara.transportationweb.Service.VehicleService;

import javassist.NotFoundException;

@RestController
@RequestMapping(value = "/api")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private TransportService transportService;

	@PostMapping("/vehicleRegistration")
	public String registerVehicle(@ModelAttribute("vehicle") VehiclesModel vehicle, @RequestParam("email") String email,
			Model model) {
		// Find the user by email
		TransportsModel transport = transportService.findByEmail(email);
		if (transport != null) {
			// Set the transport ID in the vehicle registration
			vehicle.setTransport(transport);

			if (vehicleService.registerVehicle(vehicle)) {
				// Registration successful
				return "registrationSuccess";
			} else {
				// Error in registration
				model.addAttribute("errorMessage", "License plate number already exists");
				return "registrationForm";
			}
		} else {
			// Handle case where user with the given email is not found
			model.addAttribute("errorMessage", "User not found");
			return "registrationForm";
		}
	}

	@GetMapping("/allvehicles")
	public List<VehiclesModel> getProperties() {
		// Query the database to get property data
		return vehicleService.findAllTrucks();
	}

	@GetMapping("/vehicles")
	public ResponseEntity<List<VehiclesModel>> getVehiclesByEmail(@RequestParam("email") String email) {
		List<VehiclesModel> vehicles = vehicleService.getVehiclesByEmail(email);
		if (vehicles.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(vehicles);
	}

	@GetMapping("/vehiclesByTransportId")
	public ResponseEntity<List<VehiclesModel>> getVehiclesByTransportId(@RequestParam String transportId)
			throws NotFoundException {
		List<VehiclesModel> vehicles = vehicleService.getVehiclesByTransportId(transportId);
		return ResponseEntity.ok(vehicles);
	}

	@GetMapping("/vehiclesByDriverId")
	public ResponseEntity<List<VehiclesModel>> getVehiclesByDriverId(@RequestParam String driverId) {
		if (driverId == null || driverId.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}

		List<VehiclesModel> vehicles = vehicleService.getVehiclesByDriverId(driverId);
		if (vehicles.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(vehicles);
	}

}
