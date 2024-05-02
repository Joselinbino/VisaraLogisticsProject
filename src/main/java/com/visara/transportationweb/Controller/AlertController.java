package com.visara.transportationweb.Controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.DriverModel;
import com.visara.transportationweb.Model.DriverResponse;
import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Model.VehiclesModel;
import com.visara.transportationweb.Repo.CfsRepository;
import com.visara.transportationweb.Repo.DriverRepository;
import com.visara.transportationweb.Repo.OrderBookingRepository;
import com.visara.transportationweb.Repo.VehicleRepository;
import com.visara.transportationweb.Service.WhatsAppService;

@RestController
@RequestMapping(value = "/api")
public class AlertController {
	@Autowired
	private FirebaseMessaging firebaseMessaging;

	@Autowired
	private CfsRepository cfsRepository;

	@Autowired
	private OrderBookingRepository orderBookingRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private WhatsAppService whatsappService;

	@PostMapping("/send-alert")
	public ResponseEntity<Map<String, Object>> sendAlert(@RequestBody MessageBody messageBody) {
		String message = messageBody.getMessage();
		String driverId = messageBody.getDriverId();
		String CFSemail = messageBody.getCfsEmail();
		String Departure = messageBody.getDeparture();
		String Deliver = messageBody.getDeliver();

		try {
			// Retrieve the driver entity from the repository
			Optional<DriverModel> driverOptional = driverRepository.findById(driverId);
			if (!driverOptional.isPresent()) {
				throw new Exception("Driver not found for ID: " + driverId);
			}
			DriverModel driver = driverOptional.get();

			// Attempt to find the CFS entity using the provided email
			CfsModel cfs = cfsRepository.findByCfsemail(CFSemail);
			if (cfs == null) {
				throw new Exception("CFS not found for email: " + CFSemail);
			}

			// Assuming Transport is associated with the Driver
			// This will depend on how your data model is set up
			TransportsModel transports = driver.getTransport();
			if (transports == null) {
				throw new Exception("Transport ID not found for driver ID: " + driverId);
			}

			VehiclesModel vehicle = vehicleRepository.findFirstByDriverDriverId(driverId);
			if (vehicle == null) {
				throw new Exception("Vehicle not found for driver ID: " + driverId);
			}

			String deviceToken = driver.getDeviceToken();
			if (deviceToken == null) {
				throw new Exception("Device token not found for driver ID: " + driverId);
			}

			// Fetch the latest order for this driver
			Pageable limit = PageRequest.of(0, 1);
			List<OrderBooking> latestOrders = orderBookingRepository
					.findLatestOrderByDriverDriverId(driver.getDriverId(), limit);

			// Proceed to create a new order if the latest order is not "Accepted"
			if (latestOrders.isEmpty() || !"Accepted".equals(latestOrders.get(0).getStatus())) {
				OrderBooking order = new OrderBooking();
				order.setDriver(driver);
				order.setOrderDate(new Date());
				order.setCfs(cfs);
				// Initially, set the order status as empty
				order.setStatus(""); // Status will be updated in a separate API based on the driver's response
				order.setTransport(transports); // Set Transport ID from Driver
				order.setVehicle(vehicle); // Set Vehicle ID from Vehicle table
				order.setDeparture(Departure);
				order.setDeliver(Deliver);

				// Save the new order
				orderBookingRepository.save(order);

				// Send a notification to the driver's device
				sendNotification(message, deviceToken);

				// Prepare the response indicating that the order was created and notification
				// was sent
				Map<String, Object> response = new HashMap<>();
				response.put("message", "Notification sent successfully");
				return ResponseEntity.ok().body(response);
			} else {
				// If the latest order is already "Accepted", do not create a new one
				return ResponseEntity.ok(Collections.singletonMap("message", "Driver has an ongoing booking."));
			}
		} catch (Exception e) {
			// If an exception occurs during the process, prepare the error response
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private void sendNotification(String message, String deviceToken) throws FirebaseMessagingException {
		// Create an instance of Firebase Notification
		Notification notification = Notification.builder().setTitle("Alert").setBody(message).build();

		// Construct the FCM message
		Message fcmMessage = Message.builder().setNotification(notification).setToken(deviceToken) // Use device token
																									// as the target
				.build();

		// Send the FCM message
		String response = firebaseMessaging.send(fcmMessage);
		System.out.println("Sending FCM message to device with token: " + deviceToken);
		System.out.println("Successfully sent message: " + response);
	}


	@PostMapping("/receive-response")
	public ResponseEntity<?> receiveDriverResponse(@RequestBody DriverResponse driverResponse) {
		DriverModel driver = driverRepository.findBydriverMobile(driverResponse.getMobileNumber());

		if (driver != null) {
			// Fetch the latest order for this driver
			Pageable limit = PageRequest.of(0, 1);
			List<OrderBooking> latestOrders = orderBookingRepository
					.findLatestOrderByDriverDriverId(driver.getDriverId(), limit);

			// If there's a latest order
			if (!latestOrders.isEmpty()) {
				OrderBooking latestOrder = latestOrders.get(0);

				// Update the order status only if it has not been accepted yet
				if (!"Accepted".equals(latestOrder.getStatus())) {
					latestOrder.setStatus(driverResponse.isAccepted() ? "Accepted" : "Rejected");
					orderBookingRepository.save(latestOrder);

					if (driverResponse.isAccepted()) {
						driver.setDriverStatus("OnProcess");
						driverRepository.save(driver); // Update driver's status
						String driverWhatsAppNumber = "whatsapp:+91" + driver.getDriverMobile();
						String statusUpdateMessage = "Your Order was confirmed with CFS ";
						whatsappService.sendWhatsAppMessage(driverWhatsAppNumber, statusUpdateMessage);

						return ResponseEntity
								.ok(Collections.singletonMap("message", "Order status updated to Accepted."));
					} else {
						return ResponseEntity
								.ok(Collections.singletonMap("message", "Order status updated to Rejected."));
					}
				} else {
					// If the latest order is already "Accepted", no need to update
					return ResponseEntity.ok(Collections.singletonMap("message", "Driver has an ongoing booking."));
				}
			} else {
				// If there's no latest order to update
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("error", "No pending order found for the driver."));
			}
		} else {
			// If driver is not found
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Driver not found"));
		}
	}

	@GetMapping("/check-response")
	public ResponseEntity<?> checkDriverResponse(@RequestParam String driverId) {
		// Assuming driverId is passed as a query parameter
		// Query the latest order for the driver
		Pageable limit = PageRequest.of(0, 1); // Limit to the first result
		List<OrderBooking> latestOrder = orderBookingRepository.findLatestOrderByDriverDriverId(driverId, limit);

		if (!latestOrder.isEmpty()) {
			OrderBooking order = latestOrder.get(0); // Get the first (and only) order
			String status = order.getStatus(); // Assuming there's a getStatus method
			return ResponseEntity.ok(Collections.singletonMap("status", status));
		} else {
			// If there are no orders for the driver, you can decide what status to return
			return ResponseEntity.ok(Collections.singletonMap("status", "no orders found"));
		}
	}

	@PostMapping("/completeOrder")
	public ResponseEntity<?> completeDriverOrders(@RequestParam("mobileNumber") String mobileNumber,

			@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
		DriverModel driver = driverRepository.findBydriverMobile(mobileNumber);

		if (driver == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found");
		}

		List<OrderBooking> acceptedOrders = orderBookingRepository.findByDriverDriverIdAndStatus(driver.getDriverId(),
				"Accepted");

		if (acceptedOrders.isEmpty()) {
			return ResponseEntity.ok("No 'Accepted' orders found for driver");
		}

		acceptedOrders.forEach(order -> {
			order.setStatus("Completed");
			order.setLatitude(latitude);
			order.setLongitude(longitude);
			orderBookingRepository.save(order);
		});

		// Update driver's status to 'ONLINE' after completing the orders
		driver.setDriverStatus("OFFLINE");
		driverRepository.save(driver);

		return ResponseEntity.ok("All 'Accepted' orders marked as 'Completed' and location saved");
	}

}

class MessageBody {
	private String message;
	private String driverId; 
	private String cfsEmail;
	private String departure;
	private String deliver;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getCfsEmail() {
		return cfsEmail;
	}

	public void setCfsEmail(String cfsEmail) {
		this.cfsEmail = cfsEmail;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}

	 
	 
	
	

}
