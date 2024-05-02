package com.visara.transportationweb.Service;

import java.util.List;

import com.visara.transportationweb.Model.VehiclesModel;

import javassist.NotFoundException;

public interface VehicleService {

	boolean registerVehicle(VehiclesModel vehicle);

	List<VehiclesModel> findAllTrucks();

	List<VehiclesModel> getVehiclesByEmail(String email);

	List<VehiclesModel> getVehiclesByTransportId(String transportId) throws NotFoundException;

	List<VehiclesModel> getVehiclesByDriverId(String driverId);

}
