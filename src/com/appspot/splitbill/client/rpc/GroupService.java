package com.appspot.splitbill.client.rpc;

import java.util.List;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.GroupThumbnail;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("groups")
public interface GroupService extends RemoteService {

	public List<GroupThumbnail> getGroupThumbs() throws ServiceException;
	public Group getGroup(long groupID) throws ServiceException;
	public void addGroup(GroupThumbnail group) throws ServiceException;
	public void removeGroup(long groupID) throws ServiceException;
	public void editGroup(GroupThumbnail group) throws ServiceException;
	
}
