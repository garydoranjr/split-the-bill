package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class NotificationEvent extends GwtEvent<NotificationEvent.NotificationHandler> {
	
	public static final Type<NotificationHandler> TYPE = new Type<NotificationHandler>();
	
	private NotificationEventType type;
	private String message;
	
	public NotificationEvent(NotificationEventType type){
		this(type, null);
	}
	
	public NotificationEvent(NotificationEventType type, String message){
		this.type = type;
		this.message = message;
	}
	
	public static interface NotificationHandler extends EventHandler {
		public void loading();
		public void done();
		public void error(String message);
	}
	
	public static enum NotificationEventType {
		LOADING, DONE, ERROR;
	}

	@Override
	protected void dispatch(NotificationHandler handler) {
		switch(type){
		case LOADING:
			handler.loading();
			break;
		case DONE:
			handler.done();
			break;
		case ERROR:
			handler.error(message);
			break;
		default:
			assert false : "Unhandled Type: " + type;
		}
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NotificationHandler> getAssociatedType() {
		return TYPE;
	}

}
