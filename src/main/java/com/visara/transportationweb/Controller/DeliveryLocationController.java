package com.visara.transportationweb.Controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.DeliveryLocationModel;
import com.visara.transportationweb.Repo.DeliveryLocationRepository;

@Component
@RestController
public class DeliveryLocationController {

	@Autowired
	private DeliveryLocationRepository deliveryLocationRepository;

	@PostConstruct
	public void init() {

		// Delete all existing records
		// locationRepository.deleteAll();

		// Create a new Location entity and set values manually
		DeliveryLocationModel location1 = new DeliveryLocationModel();
		location1.setDeliveryLocationId(1L);
		location1.setDeliveryLocationName("Joselin cfs");
		location1.setDeliveryLocationlatitude(13.029301);
		location1.setDeliveryLocationlongitude(80.2184354);

		// Create a new Location entity and set values manually
		DeliveryLocationModel location2 = new DeliveryLocationModel();
		location2.setDeliveryLocationId(2L);
		location2.setDeliveryLocationName("Visara cfs");
		location2.setDeliveryLocationlatitude(13.261198);
		location2.setDeliveryLocationlongitude(80.287287);

		// Create a new Location entity and set values manually
//		DeliveryLocationModel location3 = new DeliveryLocationModel();
//		location3.setLocationId(3L);
//		location3.setLocationName("Attipattu"); // 13.261198, 80.287287
//		location3.setLatitude(13.261198);
//		location3.setLongitude(80.287287);

		// Save the entity in the database

		deliveryLocationRepository.save(location1);
		deliveryLocationRepository.save(location2);
//		deliveryLocationRepository.save(location3);
	}

}
