package com.appspot.splitbill.client.widgets.table;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Widget;

public interface TableModel {

	public void setParent(Table parent);
	
	public int getRowCount();
	public int getColumnCount();
	public String getColumnTitle(int col);
	public Widget getWidget(int row, int col);
	public void setSort(int col, boolean ascending);
	public int getSortDirection(int col);
	
	public EntryViewer getViewer(int row);
	public EntryEditor getEditor(int row);
	public EntryEditor getAdder();
	
	public boolean delete(List<Integer> rowsToDelete);
	
	public void addTableModelChangeListener(TableModelChangeListener listener);
	public void removeTableModelChangeListener(TableModelChangeListener listener);
	
	public static interface TableModelChangeListener extends EventHandler{
		public void tableModelChanged();
	}
	
}
