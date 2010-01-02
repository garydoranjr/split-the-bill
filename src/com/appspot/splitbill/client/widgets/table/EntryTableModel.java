package com.appspot.splitbill.client.widgets.table;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public abstract class EntryTableModel<E extends Entry<E,C>, C extends Column>
										extends AbstractTableModel<E> {

	private E entryInstance;
	private List<C> columns;
	
	public EntryTableModel(E entryInstance){
		this.entryInstance = entryInstance;
		columns = Arrays.asList(entryInstance.getColumns());
	}
	
	@Override
	protected Comparator<E> getAscendingSorter(int column) {
		return entryInstance.getComparator(columns.get(column));
	}

	@Override
	public EntryEditor getAdder() {
		return getEditor(entryInstance.getCopy(), true);
	}
	
	@Override
	public EntryEditor getEditor(int row) {
		return getEditor(getEntry(row).getCopy(), false);
	}
	
	protected abstract EntryEditor getEditor(E entry, boolean forAdder);
	
	@Override
	public EntryViewer getViewer(int row) {
		return new DefaultEntryViewer<E, C>(getEntry(row));
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getColumnTitle(int col) {
		return columns.get(col).getColumnTitle();
	}

	@Override
	public Widget getWidget(int row, int col) {
		return getEntry(row).getWidget(columns.get(col));
	}

}
