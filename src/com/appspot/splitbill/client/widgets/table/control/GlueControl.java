package com.appspot.splitbill.client.widgets.table.control;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class GlueControl implements Control {

	@Override
	public Widget generateWidget() {
		return new HTML();
	}

	@Override
	public String getWidth() {
		return "100%";
	}

}
