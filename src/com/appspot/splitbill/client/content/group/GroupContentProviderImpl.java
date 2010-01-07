package com.appspot.splitbill.client.content.group;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.content.group.dash.Dashboard;
import com.appspot.splitbill.client.content.group.table.BillsTable;
import com.appspot.splitbill.client.content.group.table.PaymentsTable;
import com.appspot.splitbill.client.content.group.table.PersonTable;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.google.inject.Inject;

public class GroupContentProviderImpl implements GroupContentProvider {

	private LoginManager loginManager;
	private GroupManager groupManager;
	private EventBus eventBus;
	
	@Inject
	public GroupContentProviderImpl(LoginManager loginManager,
			GroupManager groupManager,
			EventBus eventBus){
		this.loginManager = loginManager;
		this.groupManager = groupManager;
		this.eventBus = eventBus;
	}
	
	@Override
	public GroupContent getGroupContent(final GroupContentType type, final Group group) {
		if(group == null){
			return new NullGroupContent();
		}
		switch(type){
		case MEMBERS:
			return new PersonTable(loginManager, groupManager, eventBus, group);
		case PAYMENTS:
			return new PaymentsTable(loginManager, groupManager, eventBus, group);
		case SETTINGS:
			return new Settings(group, groupManager, loginManager, eventBus);
		case OWES:
			return new OwesPanel(eventBus, group);
		case BILLS:
			return new BillsTable(loginManager, groupManager, eventBus, group);
		case DASHBOARD:
			return new Dashboard(group, eventBus, loginManager);
		default:
			assert false : type;
			return new NullGroupContent();
		}
	}

}
