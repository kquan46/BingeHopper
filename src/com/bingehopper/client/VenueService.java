package com.bingehopper.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.bingehopper.client.VenueDetails;

@RemoteServiceRelativePath("venue")
public interface VenueService extends RemoteService {
	
	  public void addVenue(int venueId) throws NotLoggedInException;
	  public void removeVenue(int venueId) throws NotLoggedInException;
	  public ArrayList<Integer> getVenues() throws NotLoggedInException;
	  
}
