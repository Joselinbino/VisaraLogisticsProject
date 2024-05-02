package com.visara.transportationweb.Controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.TrackingLocationsModel;
import com.visara.transportationweb.Repo.TrackingLocationRepository;

@Component
@RestController
public class CheckPointsLocationController {

	@Autowired
	private TrackingLocationRepository trackingLocationRepository;

	@PostConstruct
	public void init() {
		// trackingLocationRepository.deleteAll();

		TrackingLocationsModel checkPoint1 = new TrackingLocationsModel();
		checkPoint1.setCheckpointId(1L);
		checkPoint1.setCheckpointName("NTECL Township");
		checkPoint1.setCheckpointLatitude(13.25055);
		checkPoint1.setCheckpointLongitude(80.28969);

		TrackingLocationsModel checkPoint2 = new TrackingLocationsModel();
		checkPoint2.setCheckpointId(2L);
		checkPoint2.setCheckpointName("Vallur Camp");
		checkPoint2.setCheckpointLatitude(13.268297);
		checkPoint2.setCheckpointLongitude(80.325871);

//		TrackingLocationsModel checkPoint3 = new TrackingLocationsModel();
//		checkPoint3.setCheckpointId(3L);
//		checkPoint3.setCheckpointName("Attipattu");
//		checkPoint3.setCheckpointLatitude(13.261198);
//		checkPoint3.setCheckpointLongitude(80.287287);

//		trackingLocationRepository.save(checkPoint3);

		TrackingLocationsModel checkPoint3 = new TrackingLocationsModel();
		checkPoint3.setCheckpointId(3L);
		checkPoint3.setCheckpointName("NCTPS");
		checkPoint3.setCheckpointLatitude(13.248410);
		checkPoint3.setCheckpointLongitude(80.318750);

		TrackingLocationsModel checkPoint4 = new TrackingLocationsModel();
		checkPoint4.setCheckpointId(4L);
		checkPoint4.setCheckpointName("WestMambalam");
		checkPoint4.setCheckpointLatitude(13.029655);
		checkPoint4.setCheckpointLongitude(80.216949);

		trackingLocationRepository.save(checkPoint1);
		//trackingLocationRepository.save(checkPoint2);
		trackingLocationRepository.save(checkPoint3);
		trackingLocationRepository.save(checkPoint4);

	}
}