package com.appspot.splitbill.client.content.group;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class NullGroupContent implements GroupContent {

	@Override
	public Long forGroup() {
		return null;
	}

	@Override
	public Widget getPanelWidget() {
		// TODO Make this a fancy
		// loading label/spinner or something...
		return new HTML();
	}

	@Override
	public void unregister() {}

}
