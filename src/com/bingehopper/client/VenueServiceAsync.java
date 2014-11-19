package com.bingehopper.client;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VenueServiceAsync {
	
	  void addVenue(int venueId, AsyncCallback<Void> async);
	  void removeVenue(int venueId, AsyncCallback<Void> async);
	  void getVenues(AsyncCallback<ArrayList<Integer>> callback);
}
