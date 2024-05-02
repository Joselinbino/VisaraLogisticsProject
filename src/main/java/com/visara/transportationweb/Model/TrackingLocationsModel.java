package com.visara.transportationweb.Model;

import javax.persistence.*;

@Entity
@Table(name = "tracking_locations")
public class TrackingLocationsModel {

	@Id
	@Column(name = "checkpoint_id")
	private Long checkpointId;

	@Column(name = "checkpoint_name")
	private String checkpointName;

	@Column(name = "checkpoint_latitude")
	private Double checkpointLatitude;

	@Column(name = "checkpoint_longitude")
	private Double checkpointLongitude;

	public Long getCheckpointId() {
		return checkpointId;
	}

	public void setCheckpointId(Long checkpointId) {
		this.checkpointId = checkpointId;
	}

	public String getCheckpointName() {
		return checkpointName;
	}

	public void setCheckpointName(String checkpointName) {
		this.checkpointName = checkpointName;
	}

	public double getCheckpointLatitude() {
		return checkpointLatitude;
	}

	public void setCheckpointLatitude(double checkpointLatitude) {
		this.checkpointLatitude = checkpointLatitude;
	}

	public double getCheckpointLongitude() {
		return checkpointLongitude;
	}

	public void setCheckpointLongitude(double checkpointLongitude) {
		this.checkpointLongitude = checkpointLongitude;
	}

}
