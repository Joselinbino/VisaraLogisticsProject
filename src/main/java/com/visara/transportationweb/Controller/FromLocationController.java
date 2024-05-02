package com.visara.transportationweb.Controller;

import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.FromLocationModel;
import com.visara.transportationweb.Repo.FromLocationRepository;

@Component
@RestController
public class FromLocationController {

	@Autowired
	private FromLocationRepository locationRepository;

	@PostConstruct
	public void init() {

		// Delete all existing records
		// locationRepository.deleteAll();

		// Create a new Location entity and set values manually
		FromLocationModel location = new FromLocationModel();
		location.setLocationId(1L);
		location.setLocationName("Kamarajar Port Limited");
		location.setLatitude(13.26589);
		location.setLongitude(80.32932);

		// Create a new Location entity and set values manually
		FromLocationModel location2 = new FromLocationModel();
		location2.setLocationId(2L);
		location2.setLocationName("Adani Port");
		location2.setLatitude(13.31243);
		location2.setLongitude(80.34048);

		// Create a new Location entity and set values manually
		FromLocationModel location3 = new FromLocationModel();
		location3.setLocationId(3L);
		location3.setLocationName("Attipattu"); // 13.261198, 80.287287
		location3.setLatitude(13.261198);
		location3.setLongitude(80.287287);

		// Save the entity in the database

		locationRepository.save(location);
		locationRepository.save(location2);
		locationRepository.save(location3);
	}

	@GetMapping("/fetchLocationsname")
	public List<FromLocationModel> fetchLocations() {
		return locationRepository.findAll();
	}

}
