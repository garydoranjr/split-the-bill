package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GroupThumbsUpdateEvent extends GwtEvent<GroupThumbsUpdateEvent.GroupThumbsUpdateHandler> {
	
	public static final Type<GroupThumbsUpdateHandler> TYPE = new Type<GroupThumbsUpdateHandler>();
	
	public static interface GroupThumbsUpdateHandler extends EventHandler{
		public void groupThumbsUpdated();
	}

	@Override
	protected void dispatch(GroupThumbsUpdateHandler handler) {
		handler.groupThumbsUpdated();
	}

	@Override
	public Type<GroupThumbsUpdateHandler> getAssociatedType() {
		return TYPE;
	}

}
