package com.appspot.splitbill.client.widgets.table;

import java.util.Comparator;

import com.google.gwt.user.client.ui.Widget;

public interface Entry<T extends Entry<T,C>, C extends Column> {
	
	public T getCopy();
	
	public C[] getColumns();
	
	public Widget getWidget(C c);
	public Comparator<T> getComparator(C c);
	
}
