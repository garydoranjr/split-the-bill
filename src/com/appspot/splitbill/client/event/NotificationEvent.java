package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class NotificationEvent extends GwtEvent<NotificationEvent.NotificationHandler> {
	
	public static final Type<NotificationHandler> TYPE = new Type<NotificationHandler>();
	
	private NotificationEventType type;
	private String message;
	private int ms = 1000;
	
	public NotificationEvent(NotificationEventType type){
		this(type, null);
	}
	
	public NotificationEvent(NotificationEventType type, String message){
		this(type, message, 1000);
	}
	
	public NotificationEvent(NotificationEventType type, String message, int dispTimeMS){
		this.type = type;
		this.message = message;
		this.ms = dispTimeMS;
	}
	
	public static interface NotificationHandler extends EventHandler {
		public void loading();
		public void done();
		public void error(String message);
		public void notify(String message, int dispTimeMS);
		public void dismiss();
	}
	
	public static enum NotificationEventType {
		LOADING, DONE, ERROR, NOTIFY, DISMISS;
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
		case NOTIFY:
			handler.notify(message, ms);
			break;
		case DISMISS:
			handler.dismiss();
			break;
		default:
			assert false : "Unhandled Type: " + type;
		}
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NotificationHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static class Notification {
		public String message;
		public int dispTimeMS;
		public Notification(String message, int dispTimeMS){
			this.message = message;
			this.dispTimeMS = dispTimeMS;
		}
	}

}
