package com.appspot.splitbill.client.widgets.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AmountTextBox extends Composite implements Editor{

	private static AmountTextBoxUiBinder uiBinder = 
		GWT.create(AmountTextBoxUiBinder.class);
	interface AmountTextBoxUiBinder extends UiBinder<Widget, AmountTextBox> {}

	@UiField HorizontalPanel panel;
	@UiField Label symbol;
	@UiField DoubleSumTextBox box;
	
	public AmountTextBox() {
		initWidget(uiBinder.createAndBindUi(this));
		panel.setCellVerticalAlignment(symbol, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellVerticalAlignment(box, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public Double getAmount(){
		return box.getValue();
	}

	@Override
	public void addChangeListener(EditorChangeListener listener) {
		box.addChangeListener(listener);
	}

	@Override
	public boolean isEditOK() {
		return box.isEditOK();
	}

	@Override
	public void removeChangeListener(EditorChangeListener listener) {
		box.removeChangeListener(listener);
	}

}
