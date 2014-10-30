package com.bingehopper.client;

import java.io.Serializable;

public class VenueDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VenueDetails()
	{
		
	}
	
	String venueName;
	String venueAdd1;
	String venueAdd2;
	String venueCity;
	String venuePostal;
	String venuePhone;
	String venueType;
	String venueCapacity;
	
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public String getVenueAdd1() {
		return venueAdd1;
	}
	public void setVenueAdd1(String venueAdd1) {
		this.venueAdd1 = venueAdd1;
	}
	public String getVenueAdd2() {
		return venueAdd2;
	}
	public void setVenueAdd2(String venueAdd2) {
		this.venueAdd2 = venueAdd2;
	}
	public String getVenueCity() {
		return venueCity;
	}
	public void setVenueCity(String venueCity) {
		this.venueCity = venueCity;
	}
	public String getVenuePostal() {
		return venuePostal;
	}
	public void setVenuePostal(String venuePostal) {
		this.venuePostal = venuePostal;
	}
	public String getVenuePhone() {
		return venuePhone;
	}
	public void setVenuePhone(String venuePhone) {
		this.venuePhone = venuePhone;
	}
	public String getVenueType() {
		return venueType;
	}
	public void setVenueType(String venueType) {
		this.venueType = venueType;
	}
	public String getVenueCapacity() {
		return venueCapacity;
	}
	public void setVenueCapacity(String venueCapacity) {
		this.venueCapacity = venueCapacity;
	}

	

	

}
