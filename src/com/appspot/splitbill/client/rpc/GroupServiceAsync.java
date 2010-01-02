package com.appspot.splitbill.client.rpc;

import java.util.List;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.GroupThumbnail;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GroupServiceAsync {

	public void getGroupThumbs(AsyncCallback<List<GroupThumbnail>> asyncCallback);
	public void getGroup(long groupID, AsyncCallback<Group> asyncCallback);
	public void addGroup(GroupThumbnail group, AsyncCallback<Void> asyncCallback);
	public void editGroup(GroupThumbnail group, AsyncCallback<Void> asyncCallback);
	public void removeGroup(long groupID, AsyncCallback<Void> asyncCallback);
	
}
