package com.visara.transportationweb.Model;

import java.util.List;

public class TransportDetailsDTO {
	private CfsModel cfs;
	private List<VehiclesModel> vehicles;
	private List<TransportsModel> transports;

	public TransportDetailsDTO(CfsModel cfs, List<VehiclesModel> vehicles, List<TransportsModel> transports) {
		this.cfs = cfs;
		this.vehicles = vehicles;
		this.transports = transports;
	}

	public CfsModel getCfs() {
		return cfs;
	}

	public void setCfs(CfsModel cfs) {
		this.cfs = cfs;
	}

	public List<VehiclesModel> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehiclesModel> vehicles) {
		this.vehicles = vehicles;
	}

	public List<TransportsModel> getTransports() {
		return transports;
	}

	public void setTransports(List<TransportsModel> transports) {
		this.transports = transports;
	}

}
