package com.appspot.splitbill.client.widgets.table.control;

import java.util.LinkedList;
import java.util.List;

import com.appspot.splitbill.client.widgets.editor.Editor;
import com.appspot.splitbill.client.widgets.editor.Editor.EditorChangeListener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public abstract class AbstractButtonControl implements Control {

	private String text = "Control";
	private ClickHandler handler = null;
	private Editor editor = null;
	private boolean disableOnClick = false;
	private List<ButtonControlWidget> toDisable = new LinkedList<ButtonControlWidget>();
	
	@Override
	public ButtonControlWidget generateWidget() {
		final ButtonControlWidget retVal = new ButtonControlWidget(getText(), getHandler());
		if(disableOnClick){
			toDisable.add(retVal);
			retVal.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					disableAll();
				}
			});
		}
		if(editor != null){
			editor.addChangeListener(new EditorChangeListener(){
				@Override
				public void editorChanged(Editor source) {
					retVal.setEnabled(source.isEditOK());
				}
			});
			retVal.setEnabled(editor.isEditOK());
		}
		return retVal;
	}
	
	private void disableAll(){
		for(ButtonControlWidget but : toDisable){
			but.setEnabled(false);
		}
	}

	@Override
	public String getWidth() {
		return null;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setHandler(ClickHandler handler) {
		this.handler = handler;
	}

	public ClickHandler getHandler() {
		return handler;
	}

	public void setDisableOnClick(boolean disableOnClick) {
		this.disableOnClick = disableOnClick;
	}

	public boolean isDisableOnClick() {
		return disableOnClick;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public Editor getEditor() {
		return editor;
	}

}
