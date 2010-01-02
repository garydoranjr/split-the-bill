package com.appspot.splitbill.client.widgets;

import com.appspot.splitbill.client.util.Formatting;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AmountLabel extends Composite {

	private static AmountLabelUiBinder uiBinder = GWT.create(AmountLabelUiBinder.class);
	interface AmountLabelUiBinder extends UiBinder<Widget, AmountLabel> {}
	
	interface MyStyle extends CssResource {
		String neg();
		String pos();
	}
	
	@UiField MyStyle style;
	@UiField Label label;

	public AmountLabel() {
		initWidget(uiBinder.createAndBindUi(this));
		setAmount(0d);
	}
	
	public void setAmount(double amount){
		if(amount >= 0){
			label.removeStyleName(style.neg());
			label.addStyleName(style.pos());
		}else{
			label.removeStyleName(style.pos());
			label.addStyleName(style.neg());
		}
		String pretty = Formatting.formatAmount(Math.abs(amount));
		label.setText(pretty);
	}

}
