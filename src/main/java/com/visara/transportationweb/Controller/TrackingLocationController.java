package com.visara.transportationweb.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.DeliveryLocationModel;
import com.visara.transportationweb.Model.DriverModel;
import com.visara.transportationweb.Model.FromLocationModel;
import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TrackingLocationsModel;
import com.visara.transportationweb.Repo.DeliveryLocationRepository;
import com.visara.transportationweb.Repo.FromLocationRepository;
import com.visara.transportationweb.Repo.OrderBookingRepository;
import com.visara.transportationweb.Repo.TrackingLocationRepository;

@RestController
@RequestMapping(value = "/api")
public class TrackingLocationController {

	@Autowired
	private TrackingLocationRepository trackingLocationRepository;

	@Autowired
	private OrderBookingRepository orderBookingRepository;

	@Autowired
	private FromLocationRepository fromLocationRepository;

	@Autowired
	private DeliveryLocationRepository deliveryLocationRepository;

//	@GetMapping("/trackOrder")
//	public ResponseEntity<Map<String, Object>> trackOrder(@RequestParam String orderId) {
//		// Find the order
//		OrderBooking order = orderBookingRepository.findById(orderId).orElse(null);
//		if (order == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(Collections.singletonMap("error", "Order not found"));
//		}
//
//		// Get the driver from the order
//		DriverModel driver = order.getDriver();
//		if (driver == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(Collections.singletonMap("error", "Driver not found"));
//		}
//
//		// Check if the driver status is 'onprocess'
//		if (!"OnProcess".equals(driver.getDriverStatus())) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(Collections.singletonMap("error", "Driver is not on process"));
//		}
//
//		// Get the driver's latitude and longitude
//		double driverLat = driver.getLatitude();
//		double driverLong = driver.getLongtitude();
//
//		// Get the list of checkpoints
//		List<TrackingLocationsModel> checkpoints = trackingLocationRepository.findAll();
//
//		// Compare driver's location with checkpoints
//		for (TrackingLocationsModel checkpoint : checkpoints) {
//			double checkpointLat = checkpoint.getCheckpointLatitude();
//			double checkpointLong = checkpoint.getCheckpointLongitude();
//
//			// Calculate distance between driver's location and checkpoint
//			double distance = calculateDistance(driverLat, driverLong, checkpointLat, checkpointLong);
//
//			// If the distance is within half a kilometer, return the checkpoint name
//			if (distance <= 0.5) {
//				// Get departure and delivery data from the order table
//				String departure = order.getDeparture();
//				String delivery = order.getDeliver();
//
//				// Create a response map with name, departure, and delivery
//				Map<String, Object> response = new HashMap<>();
//				response.put("checkpointname", checkpoint.getCheckpointName());
//				response.put("departure", departure);
//				response.put("delivery", delivery);
//				return ResponseEntity.ok(response);
//			}
//		}
//
//		return ResponseEntity.ok(Collections.singletonMap("message", "No matching checkpoint found"));
//	}
	@GetMapping("/trackOrder")
	public ResponseEntity<Map<String, Object>> trackOrder(@RequestParam String orderId) {
		// Find the order
		OrderBooking order = orderBookingRepository.findById(orderId).orElse(null);
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "Order not found"));
		}

		// Get departure and delivery locations from the order
		String departureLocationName = order.getDeparture();
		String deliveryLocationName = order.getDeliver();

		// Find the latitude and longitude of the departure location
		FromLocationModel departureLocation = fromLocationRepository.findBylocationName(departureLocationName);
		if (departureLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "Departure location not found"));
		}
		double departureLat = departureLocation.getLatitude();
		double departureLong = departureLocation.getLongitude();

		// Find the latitude and longitude of the delivery location
		DeliveryLocationModel deliveryLocation = deliveryLocationRepository
				.findByDeliveryLocationName(deliveryLocationName);
		if (deliveryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "Delivery location not found"));
		}
		double deliveryLat = deliveryLocation.getDeliveryLocationlatitude();
		double deliveryLong = deliveryLocation.getDeliveryLocationlongitude();
		List<TrackingLocationsModel> checkpoints = trackingLocationRepository.findAll();

		List<String> checkpointNames = new ArrayList<>();
		// Calculate distances for each checkpoint
		for (TrackingLocationsModel checkpoint : checkpoints) {
			double checkpointLat = checkpoint.getCheckpointLatitude();
			double checkpointLong = checkpoint.getCheckpointLongitude();

			// Calculate distance between departure location and checkpoint
			double departureDistance = calculateDistance(departureLat, departureLong, checkpointLat, checkpointLong);

			// If the checkpoint is within the maximum distance of the departure location,
			// add it to the list
			if (departureDistance <= 4.0) {
				checkpointNames.add(checkpoint.getCheckpointName());
			}
		}

		List<String> deliveryCheckpointNames = new ArrayList<>();
		// Calculate distances for each checkpoint for delivery location
		for (TrackingLocationsModel checkpoint : checkpoints) {
			double checkpointLat = checkpoint.getCheckpointLatitude();
			double checkpointLong = checkpoint.getCheckpointLongitude();

			// Calculate distance between delivery location and checkpoint
			double deliveryDistance = calculateDistance(deliveryLat, deliveryLong, checkpointLat, checkpointLong);

			// If the checkpoint is within the maximum distance of the delivery location,
			// add it to the list
			if (deliveryDistance <= 5.0 && !checkpointNames.contains(checkpoint.getCheckpointName())) {
				deliveryCheckpointNames.add(checkpoint.getCheckpointName());
			}
		}

		// Create a response map with departure and delivery information, and checkpoint
		// names
		Map<String, Object> response = new HashMap<>();
		response.put("departure", departureLocationName);
		response.put("departureCheckpoint", checkpointNames.size() > 0 ? checkpointNames.get(0) : null);
		response.put("deliveryCheckpoint", deliveryCheckpointNames.size() > 0 ? deliveryCheckpointNames.get(0) : null);
		response.put("delivery", deliveryLocationName);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/driverLocation")
	public ResponseEntity<Map<String, Object>> getDriverLocation(@RequestParam String orderId) {
		// Find the order
		OrderBooking order = orderBookingRepository.findById(orderId).orElse(null);
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "Order not found"));
		}

		// Get the driver from the order
		DriverModel driver = order.getDriver();
		if (driver == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "Driver not found"));
		}

		// Get the driver's latitude and longitude
		double driverLat = driver.getLatitude();
		double driverLong = driver.getLongtitude();

		Map<String, Object> response = new HashMap<>();
		response.put("DriverLat", driverLat);
		response.put("DriverLong", driverLong);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/checkpointLocation")
	public ResponseEntity<Map<String, Object>> getCheckpointLocation(@RequestParam String checkpointName) {
		// Find the tracking location by checkpoint name
		TrackingLocationsModel location = trackingLocationRepository.findByCheckpointName(checkpointName);
		if (location == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "Order not found"));
		}

		// Create a map to hold the location's latitude and longitude
		Map<String, Object> locationMap = new HashMap<>();
		locationMap.put("CheckpointLatitude", location.getCheckpointLatitude());
		locationMap.put("CheckpointLongitude", location.getCheckpointLongitude());

		return ResponseEntity.ok(locationMap);
	}
	
	@GetMapping("/orderStatus")
	public ResponseEntity<?> getOrderStatus(@RequestParam String orderId) {
	    try {
	        OrderBooking order = orderBookingRepository.findByOrderId(orderId);
	        String status = order.getStatus();
	        return ResponseEntity.ok().body("{\"status\": \"" + status + "\"}");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("{\"error\": \"Failed to fetch order status\"}");
	    }
	}

	// Helper method to calculate distance between two points (in kilometers)
	private double calculateDistance(double Lat1, double Long1, double checkpointLat, double checkpointLong) {
		// The radius of the Earth in kilometers
		final double R = 6371.0;

		// Convert latitude and longitude from degrees to radians
		double lat1Rad = Math.toRadians(Lat1);
		double lon1Rad = Math.toRadians(Long1);
		double lat2Rad = Math.toRadians(checkpointLat);
		double lon2Rad = Math.toRadians(checkpointLong);

		// Calculate the differences
		double dLat = lat2Rad - lat1Rad;
		double dLon = lon2Rad - lon1Rad;

		// Calculate the distance using the Haversine formula
		double a = Math.pow(Math.sin(dLat / 2), 2)
				+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		System.out.println("Distance " + distance);

		return distance;
	}

}
