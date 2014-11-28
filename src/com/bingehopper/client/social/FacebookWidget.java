package com.bingehopper.client.social;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.HTML;

public class FacebookWidget extends HTML {

	public FacebookWidget(String aString)
	{
	    super(aString);
	}

	@Override
	protected void onLoad() {
	    try {
	            parseFBXML();

	    } catch (JavaScriptException e) {
	        System.err.println(e);
	    }
	}

	protected native void parseFBXML()
	/*-{
	    $wnd.FB.XFBML.parse();
	}-*/;

}

