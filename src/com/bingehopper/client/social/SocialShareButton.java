package com.bingehopper.client.social;

import java.util.ArrayList;

public class SocialShareButton extends SocialWidget {
	
	public SocialShareButton(String urlAndVenueName) {
		super(getCode(urlAndVenueName));
	}
	
	private static String getCode(String urlAndVenueName) {
		String[] urlAndVenueNameSeperated = new String[2];
		urlAndVenueNameSeperated = urlAndVenueName.split(":");
		String url = urlAndVenueNameSeperated[0];
		String venueName = urlAndVenueNameSeperated[1];
		
		if (url.contains("twitter")) {
			return "<a href=" + url 
					+ " class=\"twitter-share-button\" "
					+ "data-url=\"STUB\" "
					+ "data-hashtags=\"teamfantastic310, "
					+ venueName
					+ "\" >Tweet</a>";
		} else {
			return "<div class=\"fb-share-button\" data-href="
					+ url
					+ " data-text="
					+ "\"Check this venue out! "
					+ venueName
					+ "\" data-layout=\"button_count\"></div>";
		}
	}
}

