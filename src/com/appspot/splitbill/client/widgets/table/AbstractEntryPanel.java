package com.appspot.splitbill.client.widgets.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractEntryPanel<E extends Entry<E,C>, C extends Column> extends Composite implements EntryViewer {

	private static AbstractEntryPanelUiBinder uiBinder = 
		GWT.create(AbstractEntryPanelUiBinder.class);
	@SuppressWarnings("unchecked")
	interface AbstractEntryPanelUiBinder extends UiBinder<Widget, AbstractEntryPanel> {}
	
	interface MyStyle extends CssResource {
		String label();
		String dataLabel();
	}

	@UiField MyStyle style;
	@UiField FlexTable table;
	
	public AbstractEntryPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		table.setCellSpacing(5);
	}
	
	@Override
	public Widget getPanelWidget(){
		return this;
	}
	
	public final void addEntryColumn(Widget label, Widget entryColumn){
		int rows = table.getRowCount();
		table.setWidget(rows, 0, label);
		table.setWidget(rows, 1, entryColumn);
		label.addStyleName(style.label());
		if(entryColumn instanceof Label){
			entryColumn.addStyleName(style.dataLabel());
		}
		table.getRowFormatter().setVerticalAlign(rows, HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	}

}
