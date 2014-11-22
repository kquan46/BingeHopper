package com.bingehopper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	public List<VenueDetails> getPrices() {

		URL url;

		HttpURLConnection conn;

		BufferedReader rd;

		String line;

		String splitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

		List<VenueDetails> listOfVenues = new ArrayList<VenueDetails>();

		try {
			// converts the DataBC url to a URL object in the java.net package
			url = new URL(
					"http://www.pssg.gov.bc.ca/lclb/docs-forms/web_all.csv");

			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			// initiates input stream
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			// start a counter to assign id's as VenueDetails objects are
			// created
			int idAssign = 1;

			// for every iteration of this while loop, line is a String which
			// represents a single line in our csv file (each line is a
			// location)
			while ((line = rd.readLine()) != null) {

				line += ",N/A";

				// venue is an array of strings such that venue[i] is the i'th
				// item in the current line, so venue[0] is the name of
				// establishment, venue[1] is the address line 1 (refer to csv)
				String[] venue = line.split(splitBy);
				for (int i = 0; i< venue.length; i++) {
					if (venue[i].isEmpty())
						venue[i] = "N/A";
				}
				if (!venue[0].equals("N/A") && !venue[0].equals("establishment name")) {
					VenueDetails location = new VenueDetails(venue[0],
							venue[1], venue[2], venue[3], venue[4], venue[10],
							venue[11], venue[12], idAssign);


					listOfVenues.add(location);
					idAssign++;
				}
			}

			rd.close();

		}

		catch (IOException e) {

			e.printStackTrace();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		// VenueDetails[] arrayOfVenues = new VenueDetails[listOfVenues.size()];
		// arrayOfVenues = listOfVenues.toArray(arrayOfVenues);
		// ArrayList<String> names = new ArrayList<String>();
		// for (VenueDetails venue : listOfVenues){
		// String name = venue.getVenueName();
		// names.add(name);
		// }
		// System.out.println(names);
		return listOfVenues;

	}

}
