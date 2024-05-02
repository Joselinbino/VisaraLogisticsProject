package com.visara.transportationweb.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Service.CfsService;

@RestController
public class CfsController {

	@Autowired
	private CfsService cfsService; // Assuming you have a service to handle CFS-related operations

	@GetMapping("/get-cfs-id")
	public String getCfsIdByEmail(@RequestParam String email) {
		// Assuming you have a method in the CfsService to retrieve CFS ID by email
		String cfsId = cfsService.getCfsIdByEmail(email);
		return cfsId;
	}

	@GetMapping("/transportsByCfsemail")
	public ResponseEntity<Map<String, List<TransportsModel>>> getTransportsByCfsemail(@RequestParam String cfsemail) {
		List<TransportsModel> associatedTransports = cfsService.getAssociatedTransports(cfsemail);
		List<TransportsModel> unassociatedTransports = cfsService.getUnassociatedTransports(cfsemail);
		Map<String, List<TransportsModel>> response = new HashMap<>();
		response.put("associatedTransports", associatedTransports);
		response.put("unassociatedTransports", unassociatedTransports);
		return ResponseEntity.ok(response);
	}

//	@GetMapping("/fetchCfsNames")
//	public List<CfsModel> fetchCfsNames() {
//		return cfsService.findAll();
//	}

	@GetMapping("/cfs/{email}/orders")
	public List<OrderBooking> getCfsOrders(@PathVariable String email) {
		return cfsService.getOrdersByEmail(email);
	}

	@GetMapping("/getCfsName")
	public String getCfsName(@RequestParam String cfsEmail) {
		String cfsName = cfsService.getCfsNameByEmail(cfsEmail);
		if (cfsName != null) {
			return "{\"cfsName\": \"" + cfsName + "\"}";
		} else {
			return "{\"error\": \"CFS not found\"}";
		}
	}

}
