package com.bingehopper.client.social;

public class FacebookCommentBox extends FacebookWidget {

	public FacebookCommentBox(String url) {
		super(getCode(url));

	}

	private static String getCode(String url) {
		return "<fb:comments href=" + url
				+ "numposts=\"5\" colorscheme=\"light\"></fb:comments>";
	}

}
