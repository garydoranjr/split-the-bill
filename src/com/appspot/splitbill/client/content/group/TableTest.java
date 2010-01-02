package com.appspot.splitbill.client.content.group;

import java.util.List;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.PersonType;
import com.appspot.splitbill.client.Person.PersonColumn;
import com.appspot.splitbill.client.widgets.table.EntryEditor;
import com.appspot.splitbill.client.widgets.table.EntryTableModel;
import com.appspot.splitbill.client.widgets.table.Table;
import com.appspot.splitbill.client.widgets.table.Table.EntryMode;
import com.google.gwt.user.client.ui.Widget;

public class TableTest implements GroupContent {

	private Group group;
	
	public TableTest(Group group){
		this.group = group;
	}
	
	@Override
	public Long forGroup() {
		return group.getId();
	}

	@Override
	public Widget getPanelWidget() {
		Table table = new Table();
		Person p = new Person();
		p.setName("New Person");
		p.setType(PersonType.REGULAR);
		p.setWeight(1.0);
		EntryTableModel<Person, PersonColumn> model =
			new EntryTableModel<Person, PersonColumn>(p){

				@Override
				protected EntryEditor getEditor(Person entry, boolean forAdder) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void delete(List<Integer> rowsToDelete) {
					// TODO Auto-generated method stub
					
				}
			
		};
		model.setEntries(group.getPeople());
		table.setTableModel(model);
		table.setAdminControls(true);
		table.setEntryModeOnClick(EntryMode.EDIT);
		return table;
	}

	@Override
	public void unregister() {
		// TODO Auto-generated method stub

	}

}
