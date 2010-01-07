package com.appspot.splitbill.client.content.group;

import com.google.gwt.user.client.ui.Widget;

public interface GroupContent {
	public Widget getPanelWidget();
	public void unregister();
	public Long forGroup();
}
