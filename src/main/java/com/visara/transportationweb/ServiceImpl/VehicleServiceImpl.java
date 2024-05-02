package com.visara.transportationweb.ServiceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visara.transportationweb.Model.DriverModel;
import com.visara.transportationweb.Model.TransportDriverModel;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Model.VehiclesModel;
import com.visara.transportationweb.Repo.DriverRepository;
import com.visara.transportationweb.Repo.TransportDriversRepository;
import com.visara.transportationweb.Repo.VehicleRepository;
import com.visara.transportationweb.Service.TransportService;
import com.visara.transportationweb.Service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private TransportService transportService;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private TransportDriversRepository transportDriversRepository;

	@Override
	public boolean registerVehicle(VehiclesModel vehicle) {
		// Check if the license plate number already exists
		if (vehicleRepository.existsByLicensePlateNumber(vehicle.getLicensePlateNumber())) {
			return false; // License plate number already exists
		}

		// Retrieve driver information from the VehiclesModel
		String driverName = vehicle.getDriverName();
		String driverMobile = vehicle.getDriverPhone();
		TransportsModel transportId = vehicle.getTransport();

		// Save driver information to the DriverModel table
		DriverModel driver = new DriverModel();
		driver.setDriverName(driverName);
		driver.setDriverMobile(driverMobile);
		driver.setDriverStatus("OFFLINE");
		driver.setDriverregistrationDateTime(new Date());
		driver.setTransport(transportId);

		// Save the driver entity to the database
		driverRepository.save(driver);

		// Save the vehicle entity to the database
		vehicle.setRegistrationDateTime(new Date());
		vehicle.setTransport(transportId);
		vehicle.setDriver(driver);
		vehicleRepository.save(vehicle);

		TransportDriverModel transportDriverModel = new TransportDriverModel();
		transportDriverModel.setDriver(driver);
		transportDriverModel.setTransport(transportId);
		transportDriversRepository.save(transportDriverModel);

		return true; // Registration successful
	}

	@Override
	public List<VehiclesModel> findAllTrucks() {
		return vehicleRepository.findAll();
	}

	@Override
	public List<VehiclesModel> getVehiclesByEmail(String email) {
		TransportsModel transport = transportService.findByEmail(email);
		if (transport == null) {
			// Handle case where user is not found
			return Collections.emptyList();
		}
		return vehicleRepository.findByTransportTransportId(transport.getTransportId());
	}

	public List<VehiclesModel> getVehiclesByTransportId(String transportId) {
		return vehicleRepository.findByTransportTransportId(transportId);
	}

	@Override
	public List<VehiclesModel> getVehiclesByDriverId(String driverId) {
		return vehicleRepository.findByDriverDriverId(driverId);
	}

}
