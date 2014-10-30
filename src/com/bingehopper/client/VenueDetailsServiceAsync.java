package com.bingehopper.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VenueDetailsServiceAsync {

	void getPrices(AsyncCallback<VenueDetails[]> callback);

}
