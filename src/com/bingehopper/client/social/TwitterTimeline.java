package com.bingehopper.client.social;

public class TwitterTimeline extends TwitterWidget {

	public TwitterTimeline(String url) {
		super(getCode(url));

	}

	private static String getCode(String url) {
		return "<a class=\"twitter-timeline\" href="
				+ url
				+ "data-widget-id=\"534804413915738112\">#teamfantastic310 Tweets</a>";
	}

}
