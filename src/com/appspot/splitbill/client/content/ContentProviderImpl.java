package com.appspot.splitbill.client.content;

import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ContentProviderImpl implements ContentProvider {

	private GroupContentPanel panel;
	private GroupManager groupManager;
	private EventBus eventBus;
	
	@Inject
	public ContentProviderImpl(GroupContentPanel panel,
			GroupManager groupManager,
			EventBus eventBus){
		this.panel = panel;
		this.groupManager = groupManager;
		this.eventBus = eventBus;
	}
	
	@Override
	public Widget get(ContentType content) {
		switch(content){
		case CREATE:
			return new NewGroupPage(groupManager, eventBus);
		case GROUP:
			return panel;
		default:
		case WELCOME:
			return new WelcomePage();
		}
	}

}
