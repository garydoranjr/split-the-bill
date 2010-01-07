package com.appspot.splitbill.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GroupSelectedEvent extends GwtEvent<GroupSelectedEvent.GroupSelectedListener> {
	
	public static final Type<GroupSelectedListener> TYPE = new Type<GroupSelectedListener>();
	private Long selectedGroup = null;
	
	public GroupSelectedEvent(){
		this(null);
	}
	
	public GroupSelectedEvent(Long selectedGroup){
		this.selectedGroup = selectedGroup;
	}
	
	public static interface GroupSelectedListener extends EventHandler {
		public void groupSelected(Long groupID);
	}

	@Override
	protected void dispatch(GroupSelectedListener handler) {
		handler.groupSelected(selectedGroup);
	}

	@Override
	public Type<GroupSelectedListener> getAssociatedType() {
		return TYPE;
	}

}
