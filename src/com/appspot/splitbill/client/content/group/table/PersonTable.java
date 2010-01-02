package com.appspot.splitbill.client.content.group.table;

import java.util.List;

import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.LoginInfo;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.PersonType;
import com.appspot.splitbill.client.Person.PersonColumn;
import com.appspot.splitbill.client.content.group.GroupContent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.appspot.splitbill.client.widgets.editor.DoubleSumTextBox;
import com.appspot.splitbill.client.widgets.editor.Editor;
import com.appspot.splitbill.client.widgets.editor.MinimumLengthTextBox;
import com.appspot.splitbill.client.widgets.table.AbstractEntryEditor;
import com.appspot.splitbill.client.widgets.table.EntryEditor;
import com.appspot.splitbill.client.widgets.table.EntryTableModel;
import com.appspot.splitbill.client.widgets.table.Table;
import com.appspot.splitbill.client.widgets.table.Table.EntryMode;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PersonTable implements GroupContent, GroupUpdateHandler{

	private LoginManager loginManager;
	private GroupManager groupManager;
	private Group group;
	private EntryTableModel<Person, PersonColumn> model;
	private HandlerRegistration registration;
	
	public PersonTable(LoginManager loginManager,
			GroupManager groupManager,
			EventBus eventBus,
			Group group){
		this.loginManager = loginManager;
		this.groupManager = groupManager;
		this.group = group;
		model = new PersonTableModel(Person.newInstance());
		model.setEntries(group.getPeople());
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
			table.setEntryModeOnClick(admin ? EntryMode.EDIT : EntryMode.VIEW);
			return table;
		}catch(Exception e){
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
				type == GroupUpdateType.PERSONS){
			model.setEntries(group.getPeople());
		}
	}

	public class PersonTableModel extends EntryTableModel<Person, PersonColumn> {

		public PersonTableModel(Person entryInstance) {
			super(entryInstance);
		}

		@Override
		protected EntryEditor getEditor(Person entry, boolean forAdder) {
			return new PersonEditor(entry, forAdder);
		}

		@Override
		public void delete(List<Integer> rowsToDelete) {
			int rowCount = rowsToDelete.size();
			if(rowCount > 0 && promptDelete(rowCount)){
				for(Integer i : rowsToDelete){
					Person p = getEntry(i);
					groupManager.removePerson(group.getId(), p.getID());
				}
			}
		}
		
	}

	public class PersonEditor extends AbstractEntryEditor<Person, PersonColumn> {

		private MinimumLengthTextBox name;
		private TextBox email;
		private DoubleSumTextBox weight;
		private CheckBox adminBox;
		
		private Person entry;
		
		public PersonEditor(Person entry,
							boolean forAdder) {
			super(entry, forAdder);
			this.entry = entry;
		}
		
		@Override
		protected void initEntry(Person entry) {
			name = new MinimumLengthTextBox(1);
			name.setValue(entry.getName());
			
			email = new TextBox();
			ClientUser u = entry.getUser();
			if(u != null){
				email.setValue(u.getEmail());
			}
			
			weight = new DoubleSumTextBox();
			weight.setValue(entry.getWeight());
			
			adminBox = new CheckBox();
			adminBox.setValue(entry.getType() == PersonType.ADMIN);
			
			LoginInfo loginInfo = loginManager.getInfo();
			if(loginInfo != null && loginInfo.isUserLoggedIn(u)){
				adminBox.setEnabled(false);
				email.setEnabled(false);
			}
		}

		@Override
		protected Editor getEditor(PersonColumn column) {
			switch(column){
			case NAME:
				return name;
			case WEIGHT:
				return weight;
			default:
				return null;
			}
		}

		@Override
		protected Widget getEditorWidget(PersonColumn column) {
			switch(column){
			case ADMIN:
				return adminBox;
			case NAME:
				return name;
			case USER:
				return email;
			case WEIGHT:
				return weight;
			case PERCENT:
			default:
				return null;
			}
		}
		
		private Person fillFields(){
			Person p = entry.getCopy();
			p.setName(name.getValue());
			p.setWeight(weight.getValue());
			p.setType(adminBox.getValue()?PersonType.ADMIN:PersonType.REGULAR);
			
			String e = email.getValue();
			if(e != null && !e.equals("")){
				ClientUser user = new ClientUser();
				user.setEmail(e);
				p.setUser(user);
			}
			
			return p;
		}

		@Override
		protected void saveEdit() {
			Person p = fillFields();
			groupManager.editPerson(group.getId(), p);
		}

		@Override
		protected void saveNew() {
			Person p = fillFields();
			groupManager.addPerson(group.getId(), p);
		}

		@Override
		public void close() {}
		
	}
	
}
