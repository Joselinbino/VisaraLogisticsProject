
package com.visara.transportationweb.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "VehiclesList")
public class VehiclesModel {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vehicle_id")
	private String vehicleId;

	private String vehicleModel; // For storing the vehicle model name as a string.

	private int yearOfManufacture; // For storing the year of manufacture as an integer.

	private String licensePlateNumber; // For storing the license plate number as a string.

	private String insuranceCompany; // For storing the insurance company name as a string.

	private String insurancePolicyNumber; // For storing the insurance policy number as a string.

	private int insuranceValidity; // For storing the validity of the insurance policy as a integer.

	private String driverName; // For storing the owner's name as a string.

	private String transportPhone; // For storing the owner's email address as a string.

	private String driverPhone; // For storing the owner's phone number as a string.

	private String transportName; // For storing the transport name as a string.

	private String transportLocation; // For storing the transport location as a string.

	private int fleetSize; // For storing the fleet size as an integer.

	private double vehicleCapacity; // For storing the vehicle capacity as a floating-point number.

	private double vehiclePrice; // For storing the vehicle price as a floating-point number.

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transport_id")
	private TransportsModel transport;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id")
	private DriverModel driver;

	// Constructor
	public VehiclesModel() {
		// Generate a custom Transport ID
		this.vehicleId = generateVehicleId();
	}

	// Method to generate custom Transport ID
	private String generateVehicleId() {
		// Logic to generate a unique Transport ID,
		String prefix = "VECH";
		int randomNumber = (int) (Math.random() * 900) + 100; // Generate a random number between 100 and 999
		return prefix + randomNumber;
	}

	public DriverModel getDriver() {
		return driver;
	}

	public void setDriver(DriverModel driver) {
		this.driver = driver;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public String getInsurancePolicyNumber() {
		return insurancePolicyNumber;
	}

	public void setInsurancePolicyNumber(String insurancePolicyNumber) {
		this.insurancePolicyNumber = insurancePolicyNumber;
	}

	public int getInsuranceValidity() {
		return insuranceValidity;
	}

	public void setInsuranceValidity(int insuranceValidity) {
		this.insuranceValidity = insuranceValidity;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getTransportPhone() {
		return transportPhone;
	}

	public void setTransportPhone(String transportPhone) {
		this.transportPhone = transportPhone;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getTransportLocation() {
		return transportLocation;
	}

	public void setTransportLocation(String transportLocation) {
		this.transportLocation = transportLocation;
	}

	public int getFleetSize() {
		return fleetSize;
	}

	public void setFleetSize(int fleetSize) {
		this.fleetSize = fleetSize;
	}

	public double getVehicleCapacity() {
		return vehicleCapacity;
	}

	public void setVehicleCapacity(double vehicleCapacity) {
		this.vehicleCapacity = vehicleCapacity;
	}

	public double getVehiclePrice() {
		return vehiclePrice;
	}

	public void setVehiclePrice(double vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}

	public Date getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(Date registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}

	public TransportsModel getTransport() {
		return transport;
	}

	public void setTransport(TransportsModel transport) {
		this.transport = transport;
	}

}
