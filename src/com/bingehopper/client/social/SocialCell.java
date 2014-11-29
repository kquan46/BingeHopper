package com.bingehopper.client.social;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SocialCell extends AbstractCell<String> {

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		/*
		 * Always do a null check on the value. Cell widgets can pass null to
		 * cells if the underlying data contains a null, or if the data arrives
		 * out of order.
		 */
		if (value == null) {
			return;
		}
	}
}
