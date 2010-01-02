package com.appspot.splitbill.client.widgets.table.control;

import java.util.List;

import com.appspot.splitbill.client.widgets.table.TableModel;
import com.appspot.splitbill.client.widgets.table.Table.TableWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class DeleteSelectedControl extends AbstractButtonControl {

	public DeleteSelectedControl(final TableWidget widget, final TableModel model){
		setText("Delete");
		setHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				List<Integer> rows = widget.getSelected();
				model.delete(rows);
			}
		});
	}
	
}
