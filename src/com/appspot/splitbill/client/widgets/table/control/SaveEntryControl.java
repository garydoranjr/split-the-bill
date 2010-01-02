package com.appspot.splitbill.client.widgets.table.control;

import com.appspot.splitbill.client.widgets.table.EntryEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class SaveEntryControl extends AbstractButtonControl {

	private EntryEditor editor;
	
	public SaveEntryControl(EntryEditor editor){
		this.editor = editor;
		setText("Save");
		setDisableOnClick(true);
		setEditor(editor);
		setHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					SaveEntryControl.this.editor.save();
				}
			});
	}
	
}
