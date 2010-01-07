package com.appspot.splitbill.client.content.group.table;

import java.util.List;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Gets;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Gets.GetsColumn;
import com.appspot.splitbill.client.content.group.GroupContent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.appspot.splitbill.client.widgets.PersonListBox;
import com.appspot.splitbill.client.widgets.editor.DoubleSumTextBox;
import com.appspot.splitbill.client.widgets.editor.Editor;
import com.appspot.splitbill.client.widgets.table.AbstractEntryEditor;
import com.appspot.splitbill.client.widgets.table.EntryEditor;
import com.appspot.splitbill.client.widgets.table.EntryTableModel;
import com.appspot.splitbill.client.widgets.table.Table;
import com.appspot.splitbill.client.widgets.table.Table.EntryMode;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class GetsTable implements GroupContent, GroupUpdateHandler{

	private LoginManager loginManager;
	private GroupManager groupManager;
	private Group group;
	private Bill bill;
	private EntryTableModel<Gets, GetsColumn> model;
	private HandlerRegistration registration;
	
	public GetsTable(LoginManager loginManager,
			GroupManager groupManager,
			EventBus eventBus,
			Group group,
			Bill bill){
		this.loginManager = loginManager;
		this.groupManager = groupManager;
		this.group = group;
		this.bill = bill;
		model = new GetsTableModel(Gets.newInstance());
		model.setEntries(bill.getGets());
		registration = eventBus.addHandler(GroupUpdateEvent.TYPE, this);
	}

	@Override
	public Long forGroup() {
		return group.getId();
	}

	@Override
	public Widget getPanelWidget() {
		try{
			boolean admin = group.isAdmin(loginManager.getInfo().getUserInfo());
			Table table = new Table();
			table.setTableModel(model);
			table.setAdminControls(admin);
			table.setEntryModeOnClick(EntryMode.VIEW);
			return table;
		}catch(Exception e){
			e.printStackTrace();
			return new HTML();
		}
	}

	@Override
	public void unregister() {
		if(registration != null){
			registration.removeHandler();
		}
	}
	
	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(group.getId() == this.group.getId() &&
				bill != null &&
				bill.getID() == this.bill.getID() &&
				type == GroupUpdateType.GETS){
			model.setEntries(bill.getGets());
		}
	}

	public class GetsTableModel extends EntryTableModel<Gets, GetsColumn> {

		public GetsTableModel(Gets entryInstance) {
			super(entryInstance);
		}

		@Override
		protected EntryEditor getEditor(Gets entry, boolean forAdder) {
			if(forAdder){
				return new GetsEditor(entry, forAdder);
			}else{
				return new EntryEditor(){
					@Override
					public void save() {}
					@Override
					public void close() {}
					@Override
					public Widget getPanelWidget() {return new HTML();}
					@Override
					public void addChangeListener(EditorChangeListener listener) {}
					@Override
					public boolean isEditOK() {return false;}
					@Override
					public void removeChangeListener(EditorChangeListener listener) {}
					
				};
			}
		}

		@Override
		public void delete(List<Integer> rowsToDelete) {
			int rowCount = rowsToDelete.size();
			if(rowCount > 0 && promptDelete(rowCount, "\"Gets\"")){
				for(Integer i : rowsToDelete){
					Gets g = getEntry(i);
					groupManager.removeGets(group.getId(), bill.getID(), g.getId());
				}
			}
		}
		
	}
	
	public class GetsEditor extends AbstractEntryEditor<Gets, GetsColumn> {

		private Gets entry;
		private PersonListBox getter;
		private DoubleSumTextBox amountBox;
		private TextBox memoBox;
		
		public GetsEditor(Gets entry, boolean forAdder) {
			super(entry, forAdder);
			if(!forAdder){
				throw new RuntimeException("Cannont edit GETS");
			}
			this.entry = entry;
		}
		
		@Override
		protected void initEntry(Gets entry) {
			getter = new PersonListBox();
			getter.setData(group.getPeople(), null);
			
			amountBox = new DoubleSumTextBox();
			amountBox.setValue(0d);
			
			memoBox = new TextBox();
			memoBox.setValue("");
		}

		@Override
		protected Editor getEditor(GetsColumn column) {
			switch(column){
			case AMOUNT:
				return amountBox;
			default:
				return null;
			}
		}

		@Override
		protected Widget getEditorWidget(GetsColumn column) {
			switch(column){
			case AMOUNT:
				return amountBox;
			case MEMBER:
				return getter;
			case MEMO:
				return memoBox;
			default:
				assert false : column;
				return null;
			}
		}

		@Override
		protected void saveNew() {
			Gets g = entry.getCopy();
			g.setDescription(memoBox.getValue());
			g.setAmount(amountBox.getValue());
			g.setPersonID(getter.getSelectedID());
			groupManager.addGet(group.getId(), bill.getID(), g);
		}
		
		@Override
		protected void saveEdit() {}

		@Override
		public void close() {}

	}
	
}
