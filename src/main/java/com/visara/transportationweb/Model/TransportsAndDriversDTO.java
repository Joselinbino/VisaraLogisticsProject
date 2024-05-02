package com.visara.transportationweb.Model;

import java.util.List;

public class TransportsAndDriversDTO {
	private String transportName;

	private String transportId;

	private String driverId;

	private int vehicleFleetSize;

	private double vehiclePrice;

	private List<VehiclesModel> vehicles;

	public String getTransportId() {
		return transportId;
	}

	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public List<VehiclesModel> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehiclesModel> vehicles) {
		this.vehicles = vehicles;
	}

	public int getVehicleFleetSize() {
		return vehicleFleetSize;
	}

	public void setVehicleFleetSize(int vehicleFleetSize) {
		this.vehicleFleetSize = vehicleFleetSize;
	}

	public double getVehiclePrice() {
		return vehiclePrice;
	}

	public void setVehiclePrice(double vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	// Constructors, getters, and setters

}
