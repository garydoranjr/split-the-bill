package com.appspot.splitbill.client.event;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GroupUpdateEvent extends GwtEvent<GroupUpdateEvent.GroupUpdateHandler> {

	public static final Type<GroupUpdateHandler> TYPE = new Type<GroupUpdateHandler>();
	
	private GroupUpdateType type;
	private Group group;
	private Bill bill;
	
	public GroupUpdateEvent(GroupUpdateType type, Group group){
		this(type, group, null);
	}
	
	public GroupUpdateEvent(GroupUpdateType type, Group group, Bill bill){
		this.type = type;
		this.group = group;
		this.bill = bill;
	}
	
	public static interface GroupUpdateHandler extends EventHandler{
		public void groupUpdated(GroupUpdateType type, Group group, Bill bill);
	}
	
	public static enum GroupUpdateType{
		GROUP, BILLS, PERSONS, PAYS, GETS;
	}

	@Override
	protected void dispatch(GroupUpdateHandler handler) {
		handler.groupUpdated(type, group, bill);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GroupUpdateHandler> getAssociatedType() {
		return TYPE;
	}

}
