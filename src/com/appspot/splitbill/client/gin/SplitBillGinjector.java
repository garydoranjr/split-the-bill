package com.appspot.splitbill.client.gin;

import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.history.HistoryStateManager;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.appspot.splitbill.client.widgets.GroupSelector;
import com.appspot.splitbill.client.widgets.NotificationArea;
import com.appspot.splitbill.client.widgets.TopLevelLayout;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(SplitBillModule.class)
public interface SplitBillGinjector extends Ginjector {
	EventBus getEventBus();
	HistoryStateManager getHistoryStateManager();
	LoginManager getLoginManager();
	GroupSelector getGroupSelector();
	TopLevelLayout getTopLevelLayout();
	NotificationArea getNotificationArea();
}
