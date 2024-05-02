package com.visara.transportationweb.Model;

import javax.persistence.*;

@Entity
@Table(name = "FromLocationDetials")
public class FromLocationModel {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long locationId;

	private String locationName;

	private double latitude;

	private double longitude;
	
	  
     

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}
