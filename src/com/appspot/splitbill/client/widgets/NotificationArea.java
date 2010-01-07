package com.appspot.splitbill.client.widgets;

import java.util.LinkedList;
import java.util.List;

import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.NotificationEvent;
import com.appspot.splitbill.client.event.NotificationEvent.Notification;
import com.appspot.splitbill.client.event.NotificationEvent.NotificationEventType;
import com.appspot.splitbill.client.event.NotificationEvent.NotificationHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NotificationArea extends Composite implements NotificationHandler{

	private static NotificationAreaUiBinder uiBinder =
		GWT.create(NotificationAreaUiBinder.class);
	interface NotificationAreaUiBinder extends UiBinder<Widget, NotificationArea> {}
	
	interface MyStyle extends CssResource {
	    String hidden();
	}
	@UiField MyStyle style;
	
	@UiField Label textArea;
	
	private EventBus eventBus;
	
	@Inject
	public NotificationArea(EventBus eventBus) {
		this.eventBus = eventBus;
		initWidget(uiBinder.createAndBindUi(this));
		textArea.setText("Loading...");
		getWidget().addStyleName(style.hidden());
		eventBus.addHandler(NotificationEvent.TYPE, this);
	}
	
	private void render(){
		boolean hide = false;
		if(error){
			String text = "An error occured";
			if(errorMessage != null){
				text += ": \""+errorMessage+"\"";
			}
			text += ". Please <a href=\"javascript:location.reload();\">refresh</a> your browser.";
			textArea.getElement().setInnerHTML(text);
		}else if(nots.size() > 0){
			Notification not = nots.get(0);
			textArea.setText(not.message);
		}else if(outstanding > 0){
			textArea.setText("Loading...");
		}else{
			hide = true;
		}
		
		if(hide){
			getWidget().addStyleName(style.hidden());
		}else{
			getWidget().removeStyleName(style.hidden());
		}
	}
	
	private boolean error = false;
	private String errorMessage;
	private List<Notification> nots = new LinkedList<Notification>();
	private int outstanding = 0;
	
	@Override
	public void done() {
		outstanding--;
		render();
	}

	@Override
	public void loading() {
		outstanding++;
		render();
	}

	@Override
	public void error(String message) {
		error = true;
		errorMessage = message;
		render();
	}

	@Override
	public void dismiss() {
		nots.remove(0);
		if(nots.size() > 0){
			Notification not = nots.get(0);
			fireDismiss(not.dispTimeMS);
		}
		render();
	}

	@Override
	public void notify(String message, int dispTimeMS) {
		Notification not = new Notification(message, dispTimeMS);
		if(nots.size() == 0){
			fireDismiss(dispTimeMS);
		}
		nots.add(not);
		render();
	}
	
	private void fireDismiss(int ms){
		Timer t = new Timer() {
			@Override
			public void run() {
				eventBus.fireEvent(new NotificationEvent(NotificationEventType.DISMISS));
			}
		};
		t.schedule(ms);
	}

}
