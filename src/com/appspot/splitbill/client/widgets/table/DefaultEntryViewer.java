package com.appspot.splitbill.client.widgets.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DefaultEntryViewer<E extends Entry<E,C>, C extends Column> extends AbstractEntryPanel<E,C> {

	public DefaultEntryViewer(E entry) {
		C[] columns = entry.getColumns();
		for(C column : columns){
			Widget label = new Label(column.getColumnTitle());
			Widget data = entry.getWidget(column);
			this.addEntryColumn(label, data);
		}
	}

	@Override
	public void close() {}

}
