package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LoggedOutEvent extends GwtEvent<LoggedOutEvent.LoggedOutHandler> {
	
	public static interface LoggedOutHandler extends EventHandler {
		public void loggedOut();
	}
	
	public static final Type<LoggedOutHandler> TYPE = new Type<LoggedOutHandler>();

	@Override
	protected void dispatch(LoggedOutHandler handler) {
		handler.loggedOut();
	}

	@Override
	public GwtEvent.Type<LoggedOutHandler> getAssociatedType() {
		return TYPE;
	}

}
