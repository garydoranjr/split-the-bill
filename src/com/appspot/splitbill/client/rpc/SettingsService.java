package com.appspot.splitbill.client.rpc;

import com.appspot.splitbill.client.EmailFrequency;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("settings")
public interface SettingsService extends RemoteService {

	public void setEmailFreq(long groupID, long personID, EmailFrequency newFreq) throws ServiceException;
	
}
