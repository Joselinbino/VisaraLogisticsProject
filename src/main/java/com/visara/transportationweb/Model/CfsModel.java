package com.visara.transportationweb.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "ContainerFreightStation")
public class CfsModel {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cfs_id")
	private String cfsId;

	private String cfsName;

	private String cfsGstnNumber;

	private String cfsPhone;

	private String cfsemail;

	private String cfsAddress;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDateTime;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDateTime;

	/*
	 * @Column(nullable = true) private Double Latitude;
	 * 
	 * @Column(nullable = true) private Double Longitude;
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "transport_cfs", joinColumns = { @JoinColumn(name = "cfs_id") }, // Correct syntax with array
			inverseJoinColumns = { @JoinColumn(name = "transport_id") } // Correct syntax with array
	)
	private Set<TransportsModel> transports = new HashSet<>();

	// Constructor
	public CfsModel() {
		// Generate a custom CFS ID
		this.cfsId = generateCfsId();
	}

	// Method to generate custom CFS ID
	private String generateCfsId() {
		// Logic to generate a unique Transport ID,
		String prefix = "CFS";
		int randomNumber = (int) (Math.random() * 900) + 100; // Generate a random number between 100 and 999
		return prefix + randomNumber;
	}

	public String getCfsId() {
		return cfsId;
	}

	public void setCfsId(String cfsId) {
		this.cfsId = cfsId;
	}

	public String getCfsName() {
		return cfsName;
	}

	public void setCfsName(String cfsName) {
		this.cfsName = cfsName;
	}

	public String getCfsGstnNumber() {
		return cfsGstnNumber;
	}

	public void setCfsGstnNumber(String cfsGstnNumber) {
		this.cfsGstnNumber = cfsGstnNumber;
	}

	public String getCfsPhone() {
		return cfsPhone;
	}

	public void setCfsPhone(String cfsPhone) {
		this.cfsPhone = cfsPhone;
	}

	public String getCfsAddress() {
		return cfsAddress;
	}

	public void setCfsAddress(String cfsAddress) {
		this.cfsAddress = cfsAddress;
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

	public String getCfsemail() {
		return cfsemail;
	}

	public void setCfsemail(String cfsemail) {
		this.cfsemail = cfsemail;
	}

	public Set<TransportsModel> getTransports() {
		return transports;
	}

	public void setTransports(Set<TransportsModel> transports) {
		this.transports = transports;
	}

	/*
	 * public double getLatitude() { return Latitude; }
	 * 
	 * public void setLatitude(double latitude) { Latitude = latitude; }
	 * 
	 * public double getLongitude() { return Longitude; }
	 * 
	 * public void setLongitude(double longitude) { Longitude = longitude; }
	 */

}
