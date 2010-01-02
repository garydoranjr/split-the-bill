package com.appspot.splitbill.client.widgets.table.control;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ButtonControlWidget extends Composite{

	private static ButtonControlUiBinder uiBinder = 
		GWT.create(ButtonControlUiBinder.class);
	interface ButtonControlUiBinder extends UiBinder<Widget, ButtonControlWidget> {}

	@UiField Button button;
	
	public ButtonControlWidget(String text) {
		this(text, null);
	}
	
	public ButtonControlWidget(String text, ClickHandler handler) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(text);
		this.addClickHandler(handler);
	}
	
	public void setEnabled(boolean enabled){
		button.setEnabled(enabled);
	}
	
	public void addClickHandler(ClickHandler handler){
		if(handler != null){
			button.addClickHandler(handler);
		}
	}

}
