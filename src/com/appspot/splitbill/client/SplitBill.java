package com.appspot.splitbill.client;

import com.appspot.splitbill.client.event.CheckLoginEvent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.LoggedInEvent;
import com.appspot.splitbill.client.event.LoggedOutEvent;
import com.appspot.splitbill.client.event.LoggedInEvent.LoggedInHandler;
import com.appspot.splitbill.client.event.LoggedOutEvent.LoggedOutHandler;
import com.appspot.splitbill.client.gin.SplitBillGinjector;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SplitBill implements EntryPoint, LoggedInHandler, LoggedOutHandler {

	private static SplitBill instance = null;
	public SplitBill(){
		instance = this;
	}
	public static SplitBill get() {
		return instance;
	}
	
	private SplitBillGinjector inject;
	private EventBus eventBus;
	private LoginManager loginManager;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		inject = GWT.create(SplitBillGinjector.class);
		eventBus = inject.getEventBus();
		inject.getHistoryStateManager();
		loginManager = inject.getLoginManager();
		inject.getNotificationArea();
		eventBus.addHandler(LoggedInEvent.TYPE, this);
		eventBus.addHandler(LoggedOutEvent.TYPE, this);
		eventBus.fireEvent(new CheckLoginEvent());
	}

	native void redirect(String url) 
	/*-{ 
	        $wnd.location.replace(url);
	}-*/;

	private void loadSplitBill() {
		RootPanel.get().add(inject.getTopLevelLayout());
	}

	@Override
	public void loggedIn() {
		loadSplitBill();
	}
	
	@Override
	public void loggedOut() {
		Window.Location.replace(loginManager.getInfo().getLoginUrl());
	}
}
