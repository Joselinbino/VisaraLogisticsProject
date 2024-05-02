package com.visara.transportationweb.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportDTO;
import com.visara.transportationweb.Service.TransportService;

@RestController
@RequestMapping("/api")
public class TransportController {

	@Autowired
	private TransportService transportService;

	@GetMapping("/transports")
	public ResponseEntity<List<TransportDTO>> getTransportsForCFS(@RequestParam String cfsemail) {
		List<TransportDTO> transportDTOs = transportService.getTransportsForCFS(cfsemail);
		if (transportDTOs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(transportDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/getTransportName")
    public String getCfsName(@RequestParam String transportEmail) {
        String transportName = transportService.getTransportNameByEmail(transportEmail);
        if (transportName != null) {
            return "{\"transportName\": \"" + transportName + "\"}";
        } else {
            return "{\"error\": \"CFS not found\"}";
        }
    }
	
	//Author SURYA
	@GetMapping("/transport/{email}/orders")
    public List<OrderBooking> getTransportOrders(@PathVariable String email) {
		//System.out.println(email);
        return transportService.getOrdersByEmail(email);
    }

}
