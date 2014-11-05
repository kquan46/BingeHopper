package com.bingehopper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.bingehopper.client.VenueDetails;
import com.bingehopper.client.VenueDetailsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VenueDetailsServiceImpl extends RemoteServiceServlet implements
		VenueDetailsService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public VenueDetails[] getPrices() {
		
		  URL url;
	      
	      HttpURLConnection conn;
	      
	      BufferedReader rd;
	      
	      String line;
	      
	      String splitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	      
	      ArrayList<VenueDetails> listOfVenues = new ArrayList<VenueDetails>();
	      
	      try 
	      {
	    	 // converts the DataBC url to a URL object in the java.net package
	         url = new URL("http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv");
	         
	         
	         conn = (HttpURLConnection) url.openConnection();
	         
	         conn.setRequestMethod("GET");
	         
	         // initiates input stream
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         
	         // for every iteration of this while loop, line is a String which represents a single line in our csv file (each line is a location)
	         while ((line = rd.readLine()) != null) 
	         {
	        	 
	        	line += ",N/A";
	        	
	        	// venue is an array of strings such that venue[i] is the i'th item in the current line, so venue[0] is the name of establishment, venue[1] is the address line 1 (refer to csv)
	            String[] venue = line.split(splitBy);
	            
	            VenueDetails location = new VenueDetails (venue[0], venue[1], venue[2], venue[3], 
	            		venue[4], venue[10], venue[11], venue[12]);
	            
	            // helper method to assign the fields of VenueDetails object
	            //setValues (location,venue);
	            
	            listOfVenues.add(location);
	            
	         }
	         
	         rd.close();
	         
	      } 
	      
	      catch (IOException e) 
	      {
	    	  
	         e.printStackTrace();
	         
	      } 
	      
	      catch (Exception e) 
	      {
	    	  
	         e.printStackTrace();
	         
	      }
	      
		VenueDetails[] arrayOfVenues = new VenueDetails[listOfVenues.size()];
		arrayOfVenues = listOfVenues.toArray(arrayOfVenues);
		return arrayOfVenues;
		
	}
	
/*	public static void setValues (VenueDetails location, String[] venue)
	   {
		   
		   location.setVenueName(venue[0]);
		   location.setVenueAdd1(venue[1]);
		   location.setVenueAdd2(venue[2]);
		   location.setVenueCity(venue[3]);
		   location.setVenuePostal(venue[4]);
		   location.setVenuePhone(venue[10]);
		   location.setVenueType(venue[11]);
		   location.setVenueCapacity(venue[12]);
		   
	   }

}*/
