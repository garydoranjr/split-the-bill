package com.appspot.splitbill.client.content.group.table;

import java.util.List;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Bill.BillColumn;
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
import com.appspot.splitbill.client.widgets.table.DefaultEntryViewer;
import com.appspot.splitbill.client.widgets.table.EntryEditor;
import com.appspot.splitbill.client.widgets.table.EntryTableModel;
import com.appspot.splitbill.client.widgets.table.EntryViewer;
import com.appspot.splitbill.client.widgets.table.Table;
import com.appspot.splitbill.client.widgets.table.Table.EntryMode;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class BillsTable implements GroupContent, GroupUpdateHandler {

	private LoginManager loginManager;
	private GroupManager groupManager;
	private EventBus eventBus;
	private Group group;
	private EntryTableModel<Bill, BillColumn> model;
	private HandlerRegistration registration;
	
	public BillsTable(LoginManager loginManager,
			GroupManager groupManager,
			EventBus eventBus,
			Group group){
		this.loginManager = loginManager;
		this.groupManager = groupManager;
		this.eventBus = eventBus;
		this.group = group;
		model = new BillsTableModel(Bill.newInstance());
		model.setEntries(group.getBills());
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
				type == GroupUpdateType.BILLS){
			model.setEntries(group.getBills());
		}
	}
	
	public class BillsTableModel extends EntryTableModel<Bill, BillColumn> {

		public BillsTableModel(Bill entryInstance) {
			super(entryInstance);
		}

		@Override
		protected EntryEditor getEditor(Bill entry, boolean forAdder) {
			if(forAdder){
				return new BillEditor(entry, true);
			}else{
				return new SpecialBillEditor(entry);
			}
		}
		
		@Override
		public EntryViewer getViewer(int row) {
			Bill entry = getEntry(row);
			if(entry.getGets().size() > 0){
				return new BillViewer(entry);
			}else{
				return new DefaultEntryViewer<Bill, BillColumn>(entry);
			}
		}

		@Override
		public boolean delete(List<Integer> rowsToDelete) {
			int rowCount = rowsToDelete.size();
			if(rowCount > 0 && promptDelete(rowCount, "Bill")){
				for(Integer i : rowsToDelete){
					Bill b = getEntry(i);
					groupManager.removeBill(group.getId(), b.getID());
				}
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	public class BillEditor extends AbstractEntryEditor<Bill, BillColumn> {

		private Bill entry;
		
		private DatePicker datePicker;
		private TextBox payeeBox;
		private TextBox descBox;
		private DoubleSumTextBox amountBox;
		private PersonListBox buyerList;
		
		public BillEditor(Bill entry, boolean forAdder) {
			super(entry, forAdder);
			this.entry = entry;
		}
		
		@Override
		protected void initEntry(Bill entry) {
			datePicker = new DatePicker();
			datePicker.setValue(entry.getDate());
			
			payeeBox = new TextBox();
			payeeBox.setValue(entry.getPayee());
			
			descBox = new TextBox();
			descBox.setValue(entry.getDescription());
			
			amountBox = new DoubleSumTextBox();
			amountBox.setValue(entry.getAmount());
			
			buyerList = new PersonListBox();
			buyerList.setData(group.getPeople(),
					group.getPersonName(entry.getBuyerID()));
		}

		@Override
		protected Editor getEditor(BillColumn column) {
			switch(column){
			case AMOUNT:
				return amountBox;
			default:
				return null;
			}
		}

		@Override
		protected Widget getEditorWidget(BillColumn column) {
			switch(column){
			case AMOUNT:
				return amountBox;
			case BUYER:
				return buyerList;
			case DATE:
				return datePicker;
			case MEMO:
				return descBox;
			case PAID_TO:
				return payeeBox;
			default:
				assert false : column;
				return null;
			}
		}
		
		private Bill fillFields(){
			Bill b = entry.getCopy();
			
			b.setDate(datePicker.getValue());
			b.setPayee(payeeBox.getText());
			b.setDescription(descBox.getText());
			b.setAmount(amountBox.getValue());
			b.setBuyerID(buyerList.getSelectedID());
			
			return b;
		}

		@Override
		protected void saveEdit() {
			Bill b = fillFields();
			groupManager.editBill(group.getId(), b);
		}

		@Override
		protected void saveNew() {
			Bill b = fillFields();
			groupManager.addBill(group.getId(), b);
		}

		@Override
		public void close() {}
		
	}
	
	public class BillViewer implements EntryViewer {

		private GetsTable gTable;
		private SpecialBillViewer viewerPanel;
		private DefaultEntryViewer<Bill, BillColumn> viewer;
		
		public BillViewer(Bill entry){
			viewer = new DefaultEntryViewer<Bill, BillColumn>(entry);
			gTable = new GetsTable(loginManager, groupManager, eventBus, group, entry);
			viewerPanel = new SpecialBillViewer();
			viewerPanel.getPanel().add(viewer.getPanelWidget());
			viewerPanel.addSeparator();
			viewerPanel.addLabel("Gets");
			viewerPanel.getPanel().add(gTable.getPanelWidget());
			viewerPanel.addSeparator();
		}
		
		@Override
		public void close() {
			viewer.close();
			gTable.unregister();
		}

		@Override
		public Widget getPanelWidget() {
			return viewerPanel;
		}
		
	}
	
	public class SpecialBillEditor implements EntryEditor {

		private GetsTable gTable;
		private SpecialBillViewer viewerPanel;
		private BillEditor editor;
		
		public SpecialBillEditor(Bill entry){
			editor = new BillEditor(entry, false);
			gTable = new GetsTable(loginManager, groupManager, eventBus, group, entry);
			viewerPanel = new SpecialBillViewer();
			viewerPanel.getPanel().add(editor.getPanelWidget());
			viewerPanel.addSeparator();
			viewerPanel.addLabel("Gets");
			viewerPanel.getPanel().add(gTable.getPanelWidget());
			viewerPanel.addSeparator();
		}
		
		@Override
		public void save() {
			editor.save();
		}

		@Override
		public void close() {
			editor.close();
			gTable.unregister();
		}

		@Override
		public Widget getPanelWidget() {
			return viewerPanel;
		}

		@Override
		public void addChangeListener(EditorChangeListener listener) {
			editor.addChangeListener(listener);
		}

		@Override
		public boolean isEditOK() {
			return editor.isEditOK();
		}

		@Override
		public void removeChangeListener(EditorChangeListener listener) {
			editor.removeChangeListener(listener);
		}
		
	}

}
