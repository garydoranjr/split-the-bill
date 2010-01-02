package com.appspot.splitbill.client.widgets.table.control;

import com.google.gwt.event.dom.client.ClickHandler;

public class NewEntryControl extends AbstractButtonControl {

	public NewEntryControl(ClickHandler handler){
		setText("New");
		setHandler(handler);
	}
	
}
