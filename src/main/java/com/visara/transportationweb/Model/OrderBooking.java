package com.visara.transportationweb.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ordersTable")
public class OrderBooking {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private String orderId;

	@Column(name = "order_date")
	private Date orderDate;

	@Column(name = "status")
	private String status;

	private Double latitude;

	private Double longitude;
	
	private String departure;
	
	private String deliver;

	@ManyToOne
	@JoinColumn(name = "transport_id", referencedColumnName = "transport_id")
	private TransportsModel transport;

	@ManyToOne
	@JoinColumn(name = "cfs_id", referencedColumnName = "cfs_id")
	private CfsModel cfs;

	@ManyToOne
	@JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
	private VehiclesModel vehicle;

	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "driver_id")
	private DriverModel driver;

	// Constructor
	public OrderBooking() {
		// Generate a custom user ID
		this.orderId = generateOrderId();
	}

	// Method to generate custom user ID
	private String generateOrderId() {
		// Logic to generate a unique user ID, for example:
		String prefix = "VIS";
		int randomNumber = (int) (Math.random() * 90000) + 10000; // Generate a random number between 100 and 999
		return prefix + randomNumber;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TransportsModel getTransport() {
		return transport;
	}

	public void setTransport(TransportsModel transport) {
		this.transport = transport;
	}

	public CfsModel getCfs() {
		return cfs;
	}

	public void setCfs(CfsModel cfs) {
		this.cfs = cfs;
	}

	public VehiclesModel getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehiclesModel vehicle) {
		this.vehicle = vehicle;
	}

	public DriverModel getDriver() {
		return driver;
	}

	public void setDriver(DriverModel driver) {
		this.driver = driver;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}
	

	// Getters and setters

}
