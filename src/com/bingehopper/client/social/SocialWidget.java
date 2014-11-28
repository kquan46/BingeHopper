package com.bingehopper.client.social;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.HTML;

public class SocialWidget extends HTML {

	public SocialWidget(String aString)
	{
	    super(aString);
	}

	@Override
	protected void onLoad() {
	    try {
	            parseFBXML();
	            parseTwitter();

	    } catch (JavaScriptException e) {
	        System.err.println(e);
	    }
	}

	protected native void parseFBXML()
	/*-{
	    $wnd.FB.XFBML.parse();
	}-*/;

	protected native void parseTwitter()
	/*-{
	    $wnd.twttr.widgets.load();
	}-*/;
}


