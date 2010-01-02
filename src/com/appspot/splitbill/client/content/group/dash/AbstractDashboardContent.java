package com.appspot.splitbill.client.content.group.dash;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDashboardContent implements DashboardContent {

	private String title;
	private List<TitleChangeListener> listeners = new LinkedList<TitleChangeListener>();
	
	public void setTitle(String title){
		this.title = title;
		for(TitleChangeListener listener : listeners){
			listener.titleChanged(title);
		}
	}
	
	@Override
	public void addTitleChangeListener(TitleChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void removeTitleChangeListener(TitleChangeListener listener) {
		listeners.remove(listener);
	}

}
