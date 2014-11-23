package com.bingehopper.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("venueDetails")
public interface VenueDetailsService extends RemoteService {

  List<VenueDetails> getPrices();
  
}