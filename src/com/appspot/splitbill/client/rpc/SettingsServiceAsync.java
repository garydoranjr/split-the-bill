package com.appspot.splitbill.client.rpc;

import com.appspot.splitbill.client.EmailFrequency;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SettingsServiceAsync {

	public void setEmailFreq(long groupID, long personID, EmailFrequency newFreq, AsyncCallback<Void> asyncCallback);
	
}
