package com.appspot.splitbill.server;

import javax.jdo.PersistenceManager;

import com.appspot.splitbill.client.EmailFrequency;
import com.appspot.splitbill.client.rpc.ServiceException;
import com.appspot.splitbill.client.rpc.SettingsService;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SettingServiceImpl extends RemoteServiceServlet implements
		SettingsService {
	private static final long serialVersionUID = -5998347787511749681L;

	@Override
	public void setEmailFreq(long groupID, long personID, EmailFrequency newFreq) throws ServiceException{
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), personID).getKey();
			PersonJDO p = pm.getObjectById(PersonJDO.class, key);
			ServiceStatic.checkThisUser(user, p.getGroup(), personID);
			p.setEmailFreq(newFreq);
		}finally{
			pm.close();
		}
	}

}
