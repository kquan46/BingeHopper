package com.bingehopper.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;



public class VenueDetails implements IsSerializable, Serializable, Comparable<VenueDetails>{

	
	private ArrayList<VenueDetails> venues;
	private String venueName;
	private String venueAdd1;
	private String venueAdd2;
	private String venueCity;
	private String venuePostal;
	private String venuePhone;
	private String venueType;
	private String venueCapacity;
	private int id;
	private boolean isBookmarked;
	private boolean isVisited;
	
	private static final long serialVersionUID = 1L;

	public VenueDetails(){
		isBookmarked = false;
		isVisited = false;
	}
	
	public VenueDetails(String venueName, String venueAdd1, 
			String venueAdd2, String venueCity, String venuePostal, 
			String venuePhone, String venueType, String venueCapacity, int id) {
		this.venueName = venueName;
		this.venueAdd1 = venueAdd1;
		this.venueAdd2 = venueAdd2;
		this.venueCity = venueCity;
		this.venuePostal = venuePostal;
		this.venuePhone = venuePhone;
		this.venueType = venueType;
		this.venueCapacity = venueCapacity;
		this.id = id;
	}
	
	
	
	public String getVenueName() {
		return this.venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public String getVenueAdd1() {
		return this.venueAdd1;
	}
	public void setVenueAdd1(String venueAdd1) {
		this.venueAdd1 = venueAdd1;
	}
	public String getVenueAdd2() {
		return this.venueAdd2;
	}
	public void setVenueAdd2(String venueAdd2) {
		this.venueAdd2 = venueAdd2;
	}
	public String getVenueCity() {
		return this.venueCity;
	}
	public void setVenueCity(String venueCity) {
		this.venueCity = venueCity;
	}
	public String getVenuePostal() {
		return this.venuePostal;
	}
	public void setVenuePostal(String venuePostal) {
		this.venuePostal = venuePostal;
	}
	public String getVenuePhone() {
		return this.venuePhone;
	}
	public void setVenuePhone(String venuePhone) {
		this.venuePhone = venuePhone;
	}
	public String getVenueType() {
		return this.venueType;
	}
	public void setVenueType(String venueType) {
		this.venueType = venueType;
	}
	public String getVenueCapacity() {
		return this.venueCapacity;
	}
	public void setVenueCapacity(String venueCapacity) {
		this.venueCapacity = venueCapacity;
	}

	public int compareTo(VenueDetails other) {
		return this.venueName.toLowerCase().compareTo(other.venueName.toLowerCase());
	}

	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public boolean isBookmarked() {
		return isBookmarked;
	}
	
	public boolean isVisited() {
		return isVisited;
	}

}
