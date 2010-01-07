package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LoggedInEvent extends GwtEvent<LoggedInEvent.LoggedInHandler> {
	
	public static final Type<LoggedInHandler> TYPE = new Type<LoggedInHandler>();
	
	public static interface LoggedInHandler extends EventHandler {
		public void loggedIn();
	}

	@Override
	protected void dispatch(LoggedInHandler handler) {
		handler.loggedIn();
	}

	@Override
	public GwtEvent.Type<LoggedInHandler> getAssociatedType() {
		return TYPE;
	}

}
