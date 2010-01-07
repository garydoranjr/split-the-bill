package com.appspot.splitbill.client.widgets.editor;

public interface Editor {
	public boolean isEditOK();
	public void addChangeListener(EditorChangeListener listener);
	public void removeChangeListener(EditorChangeListener listener);
	public static interface EditorChangeListener {
		public void editorChanged(Editor source);
	}
}
