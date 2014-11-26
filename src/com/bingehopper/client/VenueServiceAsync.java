package com.bingehopper.client;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VenueServiceAsync {
	
	  void addVenue(VenueDetails venue, AsyncCallback<Void> async);
	  void removeVenue(VenueDetails venue, AsyncCallback<Void> async);
	  void removeAllVenues(AsyncCallback<Void> async);
	  void setVisited(VenueDetails venue, AsyncCallback<Void> async);
	  void getVenues(AsyncCallback<List<VenueDetails>> callback);
}
