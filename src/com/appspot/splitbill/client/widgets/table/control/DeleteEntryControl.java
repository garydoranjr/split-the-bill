package com.appspot.splitbill.client.widgets.table.control;

import java.util.LinkedList;

import com.appspot.splitbill.client.widgets.table.EntryViewer;
import com.appspot.splitbill.client.widgets.table.TableModel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class DeleteEntryControl extends AbstractButtonControl {

	public DeleteEntryControl(final EntryViewer viewer,
			final TableModel model, final int row){
		setText("Delete");
		setHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					LinkedList<Integer> list = new LinkedList<Integer>();
					list.add(row);
					if(model.delete(list)){
						disableAll();
					}
				}
			});
	}
	
}
