package com.appspot.splitbill.client.content;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.GroupThumbnail;
import com.appspot.splitbill.client.content.group.Settings;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.rpc.GroupManager;

public class EditGroupPanel extends NewGroupPage{

	private Group group;
	private Settings parent;
	
	// TODO Refactor this mess...
	public EditGroupPanel(GroupManager groupManager, EventBus eventBus, Group toEdit, Settings parent) {
		super(groupManager, eventBus);
		this.parent = parent;
		this.group = toEdit;
		setName(toEdit.getName());
		setDesc(toEdit.getDescription());
	}

	@Override
	protected void doSave(){
		GroupThumbnail thumb = new GroupThumbnail(group.getId(), nameBox.getValue(), descBox.getValue(), null);
		getGroupManager().editGroup(thumb);
		parent.renderInfoTable(false);
	}
	
	@Override
	protected void doCancel(){
		parent.renderInfoTable(false);
	}
	
}
