package com.appspot.splitbill.client.widgets.table.control;

import com.google.gwt.event.dom.client.ClickHandler;

public class CloseEntryControl extends AbstractButtonControl {

	public CloseEntryControl(ClickHandler handler){
		setText("Close");
		setDisableOnClick(true);
		setHandler(handler);
	}
	
}
