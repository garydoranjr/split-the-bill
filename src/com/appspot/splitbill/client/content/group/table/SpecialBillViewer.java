package com.appspot.splitbill.client.content.group.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpecialBillViewer extends Composite {

	private static SpecialBillViewerUiBinder uiBinder = 
		GWT.create(SpecialBillViewerUiBinder.class);
	interface SpecialBillViewerUiBinder extends UiBinder<Widget, SpecialBillViewer> {}

	interface MyStyle extends CssResource{
		String label();
	}
	
	@UiField MyStyle style;
	@UiField VerticalPanel panel;
	
	public SpecialBillViewer() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public VerticalPanel getPanel(){
		return panel;
	}
	
	public MyStyle getStyle(){
		return style;
	}
	
	public void addSeparator(){
		panel.add(new HTML("<hr />"));
	}
	
	public void addLabel(String text){
		Label l = new Label(text);
		l.addStyleName(style.label());
		panel.add(l);
	}
	
}
