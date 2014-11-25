package com.bingehopper.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.bingehopper.client.VenueDetails;

@RemoteServiceRelativePath("venue")
public interface VenueService extends RemoteService {
	
	  public void addVenue(VenueDetails venue) throws NotLoggedInException;
	  public void removeVenue(VenueDetails venue) throws NotLoggedInException;
	  public List<VenueDetails> getVenues() throws NotLoggedInException;
	  
}
