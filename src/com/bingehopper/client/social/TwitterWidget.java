package com.bingehopper.client.social;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.HTML;

public class TwitterWidget extends HTML {

	public TwitterWidget(String aString) {
		super(aString);
	}

	@Override
	protected void onLoad() {
		try {
			parseTwitter();

		} catch (JavaScriptException e) {
			System.err.println(e);
		}
	}

	protected native void parseTwitter()
	/*-{
		$wnd.twttr.widgets.load();
	}-*/;

}