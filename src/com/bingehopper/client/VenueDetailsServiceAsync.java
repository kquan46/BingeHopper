package com.bingehopper.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VenueDetailsServiceAsync {

	void getPrices(AsyncCallback<List<VenueDetails>> callback);

}
