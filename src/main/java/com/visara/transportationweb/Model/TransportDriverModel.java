package com.visara.transportationweb.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transport_drivers")
public class TransportDriverModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transportDriverId;

	@ManyToOne
	@JoinColumn(name = "transport_id")
	private TransportsModel transport;

	@ManyToOne
	@JoinColumn(name = "driver_id")
	private DriverModel driver;

	public Long getTransportDriverId() {
		return transportDriverId;
	}

	public void setTransportDriverId(Long transportDriverId) {
		this.transportDriverId = transportDriverId;
	}

	public TransportsModel getTransport() {
		return transport;
	}

	public void setTransport(TransportsModel transport) {
		this.transport = transport;
	}

	public DriverModel getDriver() {
		return driver;
	}

	public void setDriver(DriverModel driver) {
		this.driver = driver;
	}

}
