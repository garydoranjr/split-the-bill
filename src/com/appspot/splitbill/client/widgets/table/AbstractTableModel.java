package com.appspot.splitbill.client.widgets.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.Window;

public abstract class AbstractTableModel<E> implements TableModel {

	private final List<E> entries = new ArrayList<E>();
	private final List<TableModelChangeListener> listeners = new ArrayList<TableModelChangeListener>();
	private Table parent = null;
	private Comparator<E> sorter = null;
	private Integer sortColumn = null;
	private int sortDirection = 0;
	
	@Override
	public final void addTableModelChangeListener(TableModelChangeListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public final void removeTableModelChangeListener(TableModelChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected final void fireListeners(){
		for(TableModelChangeListener listener : listeners){
			listener.tableModelChanged();
		}
	}
	
	protected abstract Comparator<E> getAscendingSorter(int column);

	@Override
	public final int getSortDirection(int col) {
		if(sortColumn != null &&
				sortColumn.intValue() == col){
			return sortDirection;
		}else{
			return 0;
		}
	}
	
	@Override
	public final void setSort(int col, boolean ascending) {
		sortColumn = new Integer(col);
		if(ascending){
			sortDirection =  1;
		}else{
			sortDirection = -1;
		}
		final Comparator<E> aComp = getAscendingSorter(col);
		sorter = new Comparator<E>(){
			@Override
			public int compare(E o1, E o2) {
				return sortDirection * aComp.compare(o1, o2);
			}
		};
		sort();
		fireListeners();
	}
	
	private final void sort(){
		if(sorter != null){
			Collections.sort(entries, sorter);
		}
	}

	@Override
	public final void setParent(Table parent) {
		this.parent = parent;
	}
	
	protected final Table getParent(){
		return this.parent;
	}
	
	public final void setEntries(List<E> entries){
		this.entries.clear();
		this.entries.addAll(entries);
		sort();
		fireListeners();
	}
	
	public final List<E> getEntries(){
		return new ArrayList<E>(entries);
	}
	
	public final E getEntry(int index){
		return entries.get(index);
	}
	
	@Override
	public int getRowCount(){
		return entries.size();
	}
	
	protected boolean promptDelete(int howMany, String... terms){
		String message = "Are you sure you want to permanently delete ";
		String term;
		if(terms.length > 0){
			term = " " + terms[Math.min(howMany, terms.length - 1)];
		}else{
			term = "";
		}
		if(howMany > 1){
			message += "these " + howMany + term + " entries.";
		}else{
			message += "this" + term + " entry.";
		}
		return Window.confirm(message);
	}

}
