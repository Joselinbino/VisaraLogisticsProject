package com.visara.transportationweb.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transports")
public class TransportsModel {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transport_id")
	private String transportId;

	private String transportName;

	private String transportGstnNumber;

	private String transportPhone;

	private String transportEmail;

	private String transportAddress;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDateTime;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDateTime;

	@OneToMany(mappedBy = "transport", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DriverModel> drivers;

	@OneToMany(mappedBy = "transport", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<VehiclesModel> vehicles;

	@ManyToMany(mappedBy = "transports", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<CfsModel> cfss = new HashSet<>();

	// Constructor
	public TransportsModel() {
		// Generate a custom Transport ID
		this.transportId = generateTransportId();
	}

	// Method to generate custom Transport ID
	private String generateTransportId() {
		// Logic to generate a unique Transport ID,
		String prefix = "TRAN";
		int randomNumber = (int) (Math.random() * 900) + 100; // Generate a random number between 100 and 999
		return prefix + randomNumber;
	}

	// Constructor
	public TransportsModel(String transportId, String transportName) {
		this.transportId = transportId;
		this.transportName = transportName;
	}

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

	public String getTransportPhone() {
		return transportPhone;
	}

	public void setTransportPhone(String transportPhone) {
		this.transportPhone = transportPhone;
	}

	public String getTransportAddress() {
		return transportAddress;
	}

	public void setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
	}

	public String getTransportGstnNumber() {
		return transportGstnNumber;
	}

	public void setTransportGstnNumber(String transportGstnNumber) {
		this.transportGstnNumber = transportGstnNumber;
	}

	public Date getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(Date registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}

	public Date getLastLoginDateTime() {
		return lastLoginDateTime;
	}

	public void setLastLoginDateTime(Date lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}

	public String getTransportEmail() {
		return transportEmail;
	}

	public void setTransportEmail(String transportEmail) {
		this.transportEmail = transportEmail;
	}

	public List<VehiclesModel> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehiclesModel> vehicles) {
		this.vehicles = vehicles;
	}

	public Set<CfsModel> getCfss() {
		return cfss;
	}

	public void setCfss(Set<CfsModel> cfss) {
		this.cfss = cfss;
	}

}
