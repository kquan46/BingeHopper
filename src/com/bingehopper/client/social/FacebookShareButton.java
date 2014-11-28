package com.bingehopper.client.social;

public class FacebookShareButton extends FacebookWidget {

	public FacebookShareButton(String url) {
		super(getCode(url));
	}

	private static String getCode(String url) {
		return "<div class=\"fb-share-button\" data-href=" + url + " data-layout=\"button_count\"></div>";

	}
}

