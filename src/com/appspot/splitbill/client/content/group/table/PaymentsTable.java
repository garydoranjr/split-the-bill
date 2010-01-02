package com.appspot.splitbill.client.content.group.table;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Pays;
import com.appspot.splitbill.client.Pays.PaysColumn;
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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class PaymentsTable implements GroupContent, GroupUpdateHandler {

	private LoginManager loginManager;
	private GroupManager groupManager;
	private Group group;
	private EntryTableModel<Pays, PaysColumn> model;
	private HandlerRegistration registration;
	
	public PaymentsTable(LoginManager loginManager,
			GroupManager groupManager,
			EventBus eventBus,
			Group group){
		this.loginManager = loginManager;
		this.groupManager = groupManager;
		this.group = group;
		model = new PaymentTableModel(Pays.newInstance());
		model.setEntries(group.getPays());
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
		if(group.getId() == this.group.getId() && type == GroupUpdateType.PAYS){
			model.setEntries(group.getPays());
		}
	}
	
	public class PaymentTableModel extends EntryTableModel<Pays, PaysColumn> {

		public PaymentTableModel(Pays entryInstance) {
			super(entryInstance);
		}

		@Override
		protected EntryEditor getEditor(Pays entry, boolean forAdder) {
			return new PaysEditor(entry, forAdder);
		}

		@Override
		public void delete(List<Integer> rowsToDelete) {
			int rowCount = rowsToDelete.size();
			if(rowCount > 0 && promptDelete(rowCount)){
				for(Integer i : rowsToDelete){
					Pays p = getEntry(i);
					groupManager.removePay(group.getId(), p.getID());
				}
			}
		}
		
	}
	
	public class PaysEditor extends AbstractEntryEditor<Pays, PaysColumn> {

		private Pays entry;
		
		private DatePicker datePicker;
		private PersonListBox payerBox;
		private PersonListBox payeeBox;
		private DoubleSumTextBox amountBox;
		private TextBox descBox;
		
		private Editor payEditor;
		
		public PaysEditor(Pays entry, boolean forAdder) {
			super(entry, forAdder);
			this.entry = entry;
		}
		
		@Override
		protected void initEntry(Pays entry) {
			datePicker = new DatePicker();
			datePicker.setValue(entry.getDate() == null ? new Date() : entry.getDate());
			
			payerBox = new PersonListBox();
			payerBox.setData(group.getPeople(), group.getPersonName(entry.getPayerID()));
			
			payeeBox = new PersonListBox();
			payeeBox.setData(group.getPeople(), group.getPersonName(entry.getPayeeID()));
			
			amountBox = new DoubleSumTextBox();
			amountBox.setValue(entry.getAmount());
			
			descBox = new TextBox();
			descBox.setValue(entry.getDescription());
			
			payEditor = new Editor(){
				{
					payerBox.addChangeHandler(new ChangeHandler(){
						@Override
						public void onChange(ChangeEvent event) {
							changed();
						}
					});
					payeeBox.addChangeHandler(new ChangeHandler(){
						@Override
						public void onChange(ChangeEvent event) {
							changed();
						}
					});
				}

				@Override
				public boolean isEditOK() {
					return payerBox.getSelectedID() != payeeBox.getSelectedID();
				}

				private void changed(){
					for(EditorChangeListener listener : listeners){
						listener.editorChanged(this);
					}
				}

				List<EditorChangeListener> listeners =
					new LinkedList<EditorChangeListener>();

				@Override
				public void addChangeListener(
						EditorChangeListener listener) {
					listeners.add(listener);
				}

				@Override
				public void removeChangeListener(
						EditorChangeListener listener) {
					listeners.remove(listener);
				}
			};
		}

		@Override
		protected Editor getEditor(PaysColumn column) {
			switch(column){
			case AMOUNT:
				return amountBox;
			case PAYER:
				return payEditor;
			default:
				return null;
			}
		}

		@Override
		protected Widget getEditorWidget(PaysColumn column) {
			switch(column){
			case AMOUNT:
				return amountBox;
			case DATE:
				return datePicker;
			case DESC:
				return descBox;
			case PAYEE:
				return payeeBox;
			case PAYER:
				return payerBox;
			default:
				assert false : column;
				return null;
			}
		}
		
		private Pays fillFields(){
			Pays p = entry.getCopy();
			
			p.setDate(datePicker.getValue());
			p.setPayerID(payerBox.getSelectedID());
			p.setPayeeID(payeeBox.getSelectedID());
			p.setAmount(amountBox.getValue());
			p.setDescription(descBox.getValue());
			
			return p;
		}

		@Override
		protected void saveEdit() {
			Pays p = fillFields();
			groupManager.editPay(group.getId(), p);
		}

		@Override
		protected void saveNew() {
			Pays p = fillFields();
			groupManager.addPay(group.getId(), p);
		}

		@Override
		public void close() {}
		
	}

}
