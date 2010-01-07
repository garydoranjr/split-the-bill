package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class HistoryStateEvent extends GwtEvent<HistoryStateEvent.HistoryStateListener> {

	public static final Type<HistoryStateListener> TYPE = new Type<HistoryStateListener>();
	
	public static interface HistoryStateListener extends EventHandler {
		public void historyStateChanged();
	}

	@Override
	protected void dispatch(HistoryStateListener handler) {
		handler.historyStateChanged();
	}

	@Override
	public Type<HistoryStateListener> getAssociatedType() {
		return TYPE;
	}

}
