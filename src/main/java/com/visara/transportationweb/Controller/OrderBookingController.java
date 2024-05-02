package com.visara.transportationweb.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Repo.OrderBookingRepository;
import com.visara.transportationweb.Service.TransportService;

@RestController
@RequestMapping(value = "/api")
public class OrderBookingController {

	@Autowired
	private OrderBookingRepository orderRepository;
	
	@Autowired
	private TransportService  transportService;

	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderBooking> getOrderDetails(@PathVariable String orderId) {
		Optional<OrderBooking> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isPresent()) {
			OrderBooking order = optionalOrder.get();
			return ResponseEntity.ok(order);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
		}
	}
	
	
	//Author SURYA
	@GetMapping("/transport/{email}/allorders")
    public List<OrderBooking> getTransportOrders(@PathVariable String email) {
		System.out.println(email);
        return transportService.getOrdersByEmail(email);
    }

}
