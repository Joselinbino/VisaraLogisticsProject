package com.visara.transportationweb.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.TransportDriverModel;
import com.visara.transportationweb.Model.TransportsAndDriversDTO;
import com.visara.transportationweb.Repo.TransportDriversRepository;
import com.visara.transportationweb.Service.TransportAndDriversService;

@RestController
public class TransportDriverController {

	@Autowired
	private TransportDriversRepository transportDriversRepository;

	@Autowired
	private TransportAndDriversService transportAndDriversService;

	@GetMapping("/transportsByDriverIds")
	public List<TransportDriverModel> getTransportsByDriverIds(@RequestParam("driverIds") List<String> driverIds) {
		return transportDriversRepository.findByDriverDriverIdIn(driverIds);
	}

	@GetMapping("/api/transportsAndVehicles")
	@ResponseBody
	public ResponseEntity<List<TransportsAndDriversDTO>> getTransports(@RequestParam String fromLocationName) {
		List<TransportsAndDriversDTO> transports = transportAndDriversService.getTransportsByLocation(fromLocationName);
		return ResponseEntity.ok().body(transports);
	}
}
