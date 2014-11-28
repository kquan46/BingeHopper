package com.bingehopper.client.social;

public class FacebookCommentBox extends FacebookWidget {

	public FacebookCommentBox(String url) {
		super(getCode(url));
		
	}

	private static String getCode(String url) {
//	    return "<fb:like href=" + url + " layout=\"standard\" action=\"like\" show_faces=\"true\" share=\"true\"></fb:like>";
		return "<fb:comments href=" + url + "numposts=\"5\" colorscheme=\"light\"></fb:comments>";
	}
	
}
