package com.appspot.splitbill.client.widgets;

import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.NotificationEvent;
import com.appspot.splitbill.client.event.NotificationEvent.NotificationHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
	
	
	@Inject
	public NotificationArea(EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		textArea.setText("Loading...");
		getWidget().addStyleName(style.hidden());
		eventBus.addHandler(NotificationEvent.TYPE, this);
	}
	
	private boolean error = false;
	private int outstanding = 0;
	
	@Override
	public void done() {
		if(--outstanding <= 0){
			outstanding = 0;
			if(!error){
				getWidget().addStyleName(style.hidden());
			}
		}
	}

	@Override
	public void loading() {
		outstanding++;
		getWidget().removeStyleName(style.hidden());
	}

	@Override
	public void error(String message) {
		error = true;
		String text = "An error occured";
		if(message != null){
			text += ": \""+message+"\"";
		}
		text += ". Please refresh your browser.";
		textArea.setText(text);
		getWidget().removeStyleName(style.hidden());
	}

}
