package com.appspot.splitbill.client.widgets.table;

import com.appspot.splitbill.client.widgets.editor.Editor;
import com.appspot.splitbill.client.widgets.editor.EditorGroup;
import com.appspot.splitbill.client.widgets.editor.EditorGroupImpl;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractEntryEditor<E extends Entry<E,C>, C extends Column> extends AbstractEntryPanel<E, C> implements
		EntryEditor {

	private boolean forAdder;
	private EditorGroup editorGroup = new EditorGroupImpl();
	
	public AbstractEntryEditor(E entry){
		this(entry, false);
	}
	
	public AbstractEntryEditor(E entry, boolean forAdder) {
		this.forAdder = forAdder;
		initEntry(entry);
		for(C column : entry.getColumns()){
			Label l = new Label(column.getColumnTitle());
			Widget w = getEditorWidget(column);
			if(w != null){
				addEntryColumn(l, w);
			}else{
				addEntryColumn(l, new HTML());
			}
			Editor e = getEditor(column);
			if(e != null){
				editorGroup.addEditor(e);
			}
		}
	}
	
	public final boolean isForAdder(){
		return forAdder;
	}

	@Override
	public final void save() {
		if(forAdder){
			saveNew();
		}else{
			saveEdit();
		}
	}
	
	protected abstract void initEntry(E entry);
	protected abstract Widget getEditorWidget(C column);
	protected abstract Editor getEditor(C column);
	protected abstract void saveEdit();
	protected abstract void saveNew();

	@Override
	public final void addChangeListener(EditorChangeListener listener) {
		editorGroup.addChangeListener(listener);
	}

	@Override
	public final boolean isEditOK() {
		return editorGroup.isEditOK();
	}

	@Override
	public final void removeChangeListener(EditorChangeListener listener) {
		editorGroup.removeChangeListener(listener);
	}

}
