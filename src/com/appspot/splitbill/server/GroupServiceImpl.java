package com.appspot.splitbill.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.splitbill.client.EmailFrequency;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.GroupThumbnail;
import com.appspot.splitbill.client.PersonType;
import com.appspot.splitbill.client.rpc.GroupService;
import com.appspot.splitbill.client.rpc.ServiceException;
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GroupServiceImpl extends RemoteServiceServlet implements
GroupService {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GroupServiceImpl.class.getName());

	@Override
	public void addGroup(GroupThumbnail group) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try {
			GroupJDO persistGroup = new GroupJDO(group.getName(),
					group.getDescription(),
					group.getPassword());
			pm.makePersistent(persistGroup);
			PersonJDO persistPerson = new PersonJDO(
					persistGroup,
					user.getNickname(),
					user, 1f,
					PersonType.ADMIN,
					EmailFrequency.NEVER);
			pm.makePersistent(persistPerson);
		} finally {
			pm.close();
		}
	}

	@Override
	public void editGroup(GroupThumbnail group) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			GroupJDO groupJDO = pm.getObjectById(GroupJDO.class, group.getId());
			ServiceStatic.checkAdmin(user, groupJDO);
			groupJDO.setName(group.getName());
			groupJDO.setDescription(group.getDescription());
			groupJDO.setPassword(group.getPassword());
		}finally{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupThumbnail> getGroupThumbs() throws ServiceException {
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		List<GroupThumbnail> groups = new ArrayList<GroupThumbnail>();
		try {
			Query q = pm.newQuery(PersonJDO.class,"user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<PersonJDO> personJDOs = (List<PersonJDO>) q.execute(ServiceStatic.getUser());
			for (PersonJDO person : personJDOs) {
				GroupJDO group = person.getGroup();
				groups.add(new GroupThumbnail(group.getId(),
						group.getName(),
						group.getDescription(),
						group.getPassword()));
			}
		} finally {
			pm.close();
		}
		return groups;
	}

	@Override
	public void removeGroup(long groupID) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try {
			GroupJDO group = pm.getObjectById(GroupJDO.class, groupID);
			ServiceStatic.checkAdmin(user, group);
			pm.deletePersistent(group);
		} finally {
			pm.close();
		}
	}

	@Override
	public Group getGroup(long groupID) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			GroupJDO group = pm.getObjectById(GroupJDO.class, groupID);
			ServiceStatic.checkMember(user, group);
			return ServiceStatic.toClientGroup(group);
		}finally{
			pm.close();
		}
	}

}
