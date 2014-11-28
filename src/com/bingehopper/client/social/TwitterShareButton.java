package com.bingehopper.client.social;

public class TwitterShareButton extends TwitterWidget {

	public TwitterShareButton(String url) {
		super(getCode(url));
	}	
	
	private static String getCode(String url) {
		
		return "<a href=" + url 
				+ " class=\"twitter-share-button\" "
				+ "data-url=\"STUB\" "
				+ "data-hashtags=\"teamfantastic310, "
				+ "\" >Tweet</a>";
	}
}
