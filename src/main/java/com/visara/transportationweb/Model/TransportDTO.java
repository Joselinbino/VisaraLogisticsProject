package com.visara.transportationweb.Model;

public class TransportDTO {
	private String transportId;
	private String transportName;
	private boolean associated; // Indicates if the transport is associated with the CFS

	// Constructor
	public TransportDTO(String transportId, String transportName, boolean associated) {
		this.transportId = transportId;
		this.transportName = transportName;
		this.associated = associated;
	}

	// Getters and setters
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

	public boolean isAssociated() {
		return associated;
	}

	public void setAssociated(boolean associated) {
		this.associated = associated;
	}
}
