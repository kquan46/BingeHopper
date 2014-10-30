package com.bingehopper.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("venueDetails")
public interface VenueDetailsService extends RemoteService {

  VenueDetails[] getPrices();
  
}