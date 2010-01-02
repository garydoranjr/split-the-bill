package com.appspot.splitbill.client.content.group.dash;

import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.google.gwt.user.client.ui.Widget;

public interface DashboardContent extends GroupUpdateHandler{

	public Widget getContentWidget();
	public String getTitle();
	public boolean wantsChrome();
	public void addTitleChangeListener(TitleChangeListener listener);
	public void removeTitleChangeListener(TitleChangeListener listener);
	
	public static interface TitleChangeListener {
		public void titleChanged(String newTitle);
	}
	
}
