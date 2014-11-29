package com.bingehopper.client;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class VenueDetails implements IsSerializable, Serializable,
		Comparable<VenueDetails> {

	private String venueName;
	private String venueAdd1;
	private String venueAdd2;
	private String venueCity;
	private String venuePostal;
	private String venuePhone;
	private String venueType;
	private String venueCapacity;
	private Boolean visited;
	private String mapAddress;
	private String symbol;

	private static final long serialVersionUID = 1L;

	public VenueDetails() {
	}

	public VenueDetails(String venueName, String venueAdd1, String venueAdd2,
			String venueCity, String venuePostal, String venuePhone,
			String venueType, String venueCapacity, Boolean visited) {
		this.venueName = venueName;
		this.venueAdd1 = venueAdd1;
		this.venueAdd2 = venueAdd2;
		this.venueCity = venueCity;
		this.venuePostal = venuePostal;
		this.venuePhone = venuePhone;
		this.venueType = venueType;
		this.venueCapacity = venueCapacity;
		this.visited = visited;
		this.mapAddress = venueAdd1 + ", " + venueCity + ", BC";
		this.symbol = venueName + " " + venueCity;
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

	public Boolean getVisited() {
		return this.visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public String getMapAddress() {
		return this.mapAddress;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public int compareTo(VenueDetails other) {
		return this.venueName.toLowerCase().compareTo(
				other.venueName.toLowerCase());
	}
}
