package com.appspot.splitbill.client.rpc;

import com.appspot.splitbill.client.LoginInfo;
import com.appspot.splitbill.client.event.CheckLoginEvent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.LoggedInEvent;
import com.appspot.splitbill.client.event.LoggedOutEvent;
import com.appspot.splitbill.client.event.CheckLoginEvent.CheckLoginHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginManager implements CheckLoginHandler{

	private EventBus eventBus;
	private LoginServiceAsync service;
	private LoginInfo info = null;
	
	@Inject
	public LoginManager(EventBus eventBus, LoginServiceAsync service){
		this.eventBus = eventBus;
		this.service = service;
		this.eventBus.addHandler(CheckLoginEvent.TYPE, this);
	}

	@Override
	public void checkLogin() {
		service.login(Window.Location.getHref(), new AsyncCallback<LoginInfo>(){
			@Override
			public void onFailure(Throwable caught) {
				// TODO LOGIN ERROR
			}
			@Override
			public void onSuccess(LoginInfo result) {
				info = result;
				if(info.isLoggedIn()){
					eventBus.fireEvent(new LoggedInEvent());
				}else{
					eventBus.fireEvent(new LoggedOutEvent());
				}
			}
		});
	}
	
	public LoginInfo getInfo(){
		return info;
	}
	
}
