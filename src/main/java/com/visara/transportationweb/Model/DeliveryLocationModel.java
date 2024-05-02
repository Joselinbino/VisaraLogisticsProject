package com.visara.transportationweb.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DeliveryLocationDetials")
public class DeliveryLocationModel {
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryLocationId;

	private String deliveryLocationName;

	private double deliveryLocationlatitude;

	private double deliveryLocationlongitude;

	public Long getDeliveryLocationId() {
		return deliveryLocationId;
	}

	public void setDeliveryLocationId(Long deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

	public String getDeliveryLocationName() {
		return deliveryLocationName;
	}

	public void setDeliveryLocationName(String deliveryLocationName) {
		this.deliveryLocationName = deliveryLocationName;
	}

	public double getDeliveryLocationlatitude() {
		return deliveryLocationlatitude;
	}

	public void setDeliveryLocationlatitude(double deliveryLocationlatitude) {
		this.deliveryLocationlatitude = deliveryLocationlatitude;
	}

	public double getDeliveryLocationlongitude() {
		return deliveryLocationlongitude;
	}

	public void setDeliveryLocationlongitude(double deliveryLocationlongitude) {
		this.deliveryLocationlongitude = deliveryLocationlongitude;
	}

}
