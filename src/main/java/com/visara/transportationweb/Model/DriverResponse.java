package com.visara.transportationweb.Model;

public class DriverResponse {
    private boolean accepted;
    private String mobileNumber; // Optional, if you need to know which driver responded

    // Constructor
    public DriverResponse() {
        // Default constructor
    }

    // Getters and setters
    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    

    public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	// toString method for logging purposes (optional)
    @Override
    public String toString() {
        return "DriverResponse{" +
                "accepted=" + accepted +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}

