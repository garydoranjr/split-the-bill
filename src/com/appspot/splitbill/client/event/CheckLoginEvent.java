package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CheckLoginEvent extends GwtEvent<CheckLoginEvent.CheckLoginHandler> {

	public static final Type<CheckLoginHandler> TYPE = new Type<CheckLoginHandler>();
	
	@Override
	protected void dispatch(CheckLoginHandler handler) {
		handler.checkLogin();
	}

	@Override
	public GwtEvent.Type<CheckLoginHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static interface CheckLoginHandler extends EventHandler {
		public void checkLogin();
	}


}
