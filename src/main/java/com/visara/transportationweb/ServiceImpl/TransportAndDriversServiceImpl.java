package com.visara.transportationweb.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visara.transportationweb.Model.DriverModel;
import com.visara.transportationweb.Model.FromLocationModel;
import com.visara.transportationweb.Model.TransportsAndDriversDTO;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Model.VehiclesModel;
import com.visara.transportationweb.Repo.DriverRepository;
import com.visara.transportationweb.Repo.FromLocationRepository;
import com.visara.transportationweb.Service.TransportAndDriversService;

@Service
public class TransportAndDriversServiceImpl implements TransportAndDriversService {

	@Autowired
	private FromLocationRepository locationRepository;

	@Autowired
	private DriverRepository driverRepository;

	public List<TransportsAndDriversDTO> getTransportsByLocation(String fromLocationName) {
		// Step 1: Get lat and long from the location table using fromLocationName
		FromLocationModel fromLocation = (FromLocationModel) locationRepository.findByLocationName(fromLocationName);
		double fromLat = fromLocation.getLatitude();
		double fromLong = fromLocation.getLongitude();

		// Step 2: Get online drivers and their locations
		List<DriverModel> onlineDrivers = driverRepository.findBydriverStatus("ONLINE");

		// Step 3: Filter drivers within 1km radius of fromLocation
		List<DriverModel> driversWithinRadius = filterDriversWithinRadius(onlineDrivers, fromLat, fromLong);

		// Step 4: Get transport details of filtered drivers
		List<TransportsModel> transports = getTransportsByDrivers(driversWithinRadius);

		// Step 5: Get vehicles of filtered drivers
		List<VehiclesModel> vehicles = getVehiclesByDrivers(driversWithinRadius);

		// Step 6: Convert data to DTOs
		List<TransportsAndDriversDTO> transportDTOs = convertToDTOs(transports, vehicles);

		return transportDTOs;
	}

	private static final double EARTH_RADIUS_KM = 6371.0; // Earth radius in kilometers

	public static List<DriverModel> filterDriversWithinRadius(List<DriverModel> drivers, double fromLat,
			double fromLong) {
		List<DriverModel> driversWithinRadius = new ArrayList<>();

		for (DriverModel driver : drivers) {
			double driverLat = driver.getLatitude();
			double driverLong = driver.getLongtitude();

			double distance = calculateDistance(fromLat, fromLong, driverLat, driverLong);
			if (distance <= 5.0) { // 1km radius
				driversWithinRadius.add(driver);
			}
		}

		return driversWithinRadius;
	}

	private static double calculateDistance(double fromLat, double fromLong, double driverLat, double driverLong) {
		// Convert latitude and longitude from degrees to radians
		double fromLatRad = Math.toRadians(fromLat);
		double fromLongRad = Math.toRadians(fromLong);
		double driverLatRad = Math.toRadians(driverLat);
		double driverLongRad = Math.toRadians(driverLong);

		// Calculate the change in coordinates
		double deltaLat = driverLatRad - fromLatRad;
		double deltaLong = driverLongRad - fromLongRad;

		// Calculate the distance using the Haversine formula
		double a = Math.pow(Math.sin(deltaLat / 2), 2)
				+ Math.cos(fromLatRad) * Math.cos(driverLatRad) * Math.pow(Math.sin(deltaLong / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = EARTH_RADIUS_KM * c;

		return distance;
	}

	public List<TransportsModel> getTransportsByDrivers(List<DriverModel> drivers) {
		List<TransportsModel> transports = new ArrayList<>();

		for (DriverModel driver : drivers) {
			TransportsModel transportsByDriver = driver.getTransport(); // Assuming a method to get transports
																		// associated with driver
			transports.add(transportsByDriver);
		}

		return transports;
	}

	public List<VehiclesModel> getVehiclesByDrivers(List<DriverModel> drivers) {
		List<VehiclesModel> vehicles = new ArrayList<>();

		for (DriverModel driver : drivers) {
			List<VehiclesModel> vehiclesByDriver = driver.getVehicles(); // Assuming a method to get vehicles associated
																			// with driver
			vehicles.addAll(vehiclesByDriver);
		}

		return vehicles;
	}

	public List<TransportsAndDriversDTO> convertToDTOs(List<TransportsModel> transports, List<VehiclesModel> vehicles) {
		List<TransportsAndDriversDTO> dtos = new ArrayList<>();

		for (TransportsModel transport : transports) {
			TransportsAndDriversDTO dto = new TransportsAndDriversDTO();
			dto.setTransportId(transport.getTransportId());
			dto.setTransportName(transport.getTransportName());

			// Find vehicles associated with this transport
			int FleetSize = 0;
			double VehiclePrice = 0.0;
			List<VehiclesModel> transportVehicles = new ArrayList<>();
			for (VehiclesModel vehicle : vehicles) {
				if (vehicle.getTransport().equals(transport)) {
					FleetSize += vehicle.getFleetSize();
					VehiclePrice += vehicle.getVehiclePrice();
					transportVehicles.add(vehicle);

					// Set driverId from vehicle to DTO
					dto.setDriverId(vehicle.getDriver().getDriverId());
				}
			}

			// Set the total fleet size and average vehicle price to the DTO
			dto.setVehicleFleetSize(FleetSize);
			dto.setVehiclePrice(VehiclePrice); // Calculate average price per vehicle
			dto.setVehicles(transportVehicles);

			dtos.add(dto);
		}

		return dtos;
	}

}
