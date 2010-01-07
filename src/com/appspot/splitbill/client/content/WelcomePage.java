package com.appspot.splitbill.client.content;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class WelcomePage extends Widget {

	private static WelcomePageUiBinder uiBinder = GWT
			.create(WelcomePageUiBinder.class);
	interface WelcomePageUiBinder extends UiBinder<Element, WelcomePage> {}

	public WelcomePage() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
