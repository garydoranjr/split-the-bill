package com.appspot.splitbill.client.event;

import com.appspot.splitbill.client.gin.RootInstance;
import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EventBus extends HandlerManager {
	
	@Inject
	public EventBus(@RootInstance Object source) {
		super(source);
	}

}
