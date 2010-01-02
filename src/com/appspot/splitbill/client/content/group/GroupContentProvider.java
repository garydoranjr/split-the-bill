package com.appspot.splitbill.client.content.group;

import com.appspot.splitbill.client.Group;

public interface GroupContentProvider {
	public GroupContent getGroupContent(GroupContentType type, Group group);
}
