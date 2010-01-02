package com.appspot.splitbill.client.event;

import com.appspot.splitbill.client.content.group.GroupContentType;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GroupContentChangeEvent extends GwtEvent<GroupContentChangeEvent.GroupContentChangeListener> {

	public static final Type<GroupContentChangeListener> TYPE = new Type<GroupContentChangeListener>();
	
	private GroupContentType type;
	
	public GroupContentChangeEvent(GroupContentType type){
		this.type = type;
	}
	
	public static interface GroupContentChangeListener extends EventHandler{
		public void groupContentChanged(GroupContentType type);
	}

	@Override
	public Type<GroupContentChangeListener> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GroupContentChangeListener handler) {
		handler.groupContentChanged(type);
	}

}
