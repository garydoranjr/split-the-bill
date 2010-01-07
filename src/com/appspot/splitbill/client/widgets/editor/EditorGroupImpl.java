package com.appspot.splitbill.client.widgets.editor;

import java.util.LinkedList;
import java.util.List;

import com.appspot.splitbill.client.widgets.editor.Editor.EditorChangeListener;

public class EditorGroupImpl implements EditorGroup, EditorChangeListener {

	private List<EditorChangeListener> listeners = new LinkedList<EditorChangeListener>();
	private List<Editor> editors = new LinkedList<Editor>();
	private boolean editOK = true;
	
	private void checkEditOK(){
		check:{
			for(Editor editor : editors){
				if(!editor.isEditOK()){
					editOK = false;
					break check;
				}
			}
			editOK = true;
			break check;
		}
		for(EditorChangeListener listener : listeners){
			listener.editorChanged(this);
		}
	}
	
	@Override
	public void addEditor(Editor editor) {
		editor.addChangeListener(this);
		editors.add(editor);
		checkEditOK();
	}

	@Override
	public void removeEditor(Editor editor) {
		editor.removeChangeListener(this);
		editors.remove(editor);
		checkEditOK();
	}

	@Override
	public void addChangeListener(EditorChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public boolean isEditOK() {
		return editOK;
	}

	@Override
	public void removeChangeListener(EditorChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void editorChanged(Editor source) {
		checkEditOK();
	}

}
