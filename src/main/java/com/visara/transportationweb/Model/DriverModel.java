package com.visara.transportationweb.Model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "DriversList")
public class DriverModel {
	@Id
	@Column(name = "driver_id")
	private String driverId;

	// Constructor
	public DriverModel() {
		// Generate a custom user ID
		this.driverId = generateUserId();
	}

	// Method to generate custom user ID
	private String generateUserId() {
		// Logic to generate a unique user ID, for example:
		String prefix = "DRIVER";
		int randomNumber = (int) (Math.random() * 900) + 100; // Generate a random number between 100 and 999
		return prefix + randomNumber;
	}

	@Column
	private String driverName;

	@Column
	private String driverMobile;

	@Column
	private String driverStatus;
	@Column
	private String deviceToken;

	@Column
	private double Latitude;
	@Column
	private double Longtitude;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date driverregistrationDateTime;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date driverlastLoginDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transport_id")
	private TransportsModel transport;

	@OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<VehiclesModel> vehicles;

	public List<VehiclesModel> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehiclesModel> vehicles) {
		this.vehicles = vehicles;
	}

	public TransportsModel getTransport() {
		return transport;
	}

	public void setTransport(TransportsModel transport) {
		this.transport = transport;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverMobile() {
		return driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}

	public String getDriverStatus() {
		return driverStatus;
	}

	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}

	public double getLatitude() {
		return Latitude;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongtitude() {
		return Longtitude;
	}

	public void setLongtitude(double longtitude) {
		Longtitude = longtitude;
	}

	public Date getDriverregistrationDateTime() {
		return driverregistrationDateTime;
	}

	public void setDriverregistrationDateTime(Date driverregistrationDateTime) {
		this.driverregistrationDateTime = driverregistrationDateTime;
	}

	public Date getDriverlastLoginDateTime() {
		return driverlastLoginDateTime;
	}

	public void setDriverlastLoginDateTime(Date driverlastLoginDateTime) {
		this.driverlastLoginDateTime = driverlastLoginDateTime;
	}

}
