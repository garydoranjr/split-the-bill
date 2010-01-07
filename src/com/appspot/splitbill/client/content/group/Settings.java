package com.appspot.splitbill.client.content.group;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.EmailFrequency;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.content.EditGroupPanel;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Settings extends Composite implements GroupContent, GroupUpdateHandler{

	private static SettingsUiBinder uiBinder = GWT.create(SettingsUiBinder.class);
	interface SettingsUiBinder extends UiBinder<Widget, Settings> {}
	
	interface MyStyle extends CssResource {
		String boldLabel();
		String label();
	}
	
	private Group group;
	private long personID = -1;
	private GroupManager groupManager;
	private EventBus eventBus;
	
	@UiField MyStyle style;
	@UiField VerticalPanel panel;
	@UiField FlexTable infoTable;
	
	@UiField HorizontalPanel editGroupPanel;
	@UiField Button editButton;
	@UiField Button deleteButton;
	
	@UiField HorizontalPanel emailFreqPanel;
	@UiField Label emailFreqLabel;
	@UiField ListBox emailFreqBox;

	public Settings(Group group, GroupManager groupManager, LoginManager loginManager, EventBus eventBus) {
		this.group = group;
		this.groupManager = groupManager;
		this.eventBus = eventBus;
		initWidget(uiBinder.createAndBindUi(this));
		
		eventBus.addHandler(GroupUpdateEvent.TYPE, this);
		
		panel.setSpacing(5);
		
		infoTable.setCellSpacing(5);
		
		renderInfoTable(false);
		
		editGroupPanel.setSpacing(5);
		ClientUser user = loginManager.getInfo().getUserInfo();
		boolean admin = group.isAdmin(user);
		editGroupPanel.setVisible(admin);
		
		emailFreqPanel.setSpacing(5);
		Person p = group.getPerson(user);
		int initial = 0;
		if(p != null){
			personID = p.getID();
			initial = p.getEmailFreq().ordinal();
		}
		for(EmailFrequency freq : EmailFrequency.values()){
			emailFreqBox.addItem(freq.getNiceName());
		}
		emailFreqBox.setSelectedIndex(initial);
	}
	
	@UiHandler("deleteButton")
	void deleteClicked(ClickEvent e){
		boolean cont = Window.confirm("Are you sure you want to permanently delete this group?");
		if(cont){
			groupManager.removeGroup(group.getId());
			deleteButton.setEnabled(false);
		}
	}
	
	@UiHandler("editButton")
	void editClicked(ClickEvent e){
		renderInfoTable(true);
	}
	
	@UiHandler("emailFreqBox")
	void onChange(ChangeEvent event){
		EmailFrequency freq = EmailFrequency.values()[emailFreqBox.getSelectedIndex()];
		groupManager.setEmailFreq(group.getId(), personID, freq);
	}
	
	public Widget getPanelWidget(){
		return this;
	}

	@Override
	public Long forGroup() {
		return group.getId();
	}

	@Override
	public void unregister() {}
	
	public void renderInfoTable(boolean forEdit){
		infoTable.clear();
		editButton.setEnabled(!forEdit);
		if(forEdit){
			infoTable.setWidget(0, 0, new EditGroupPanel(groupManager, eventBus, group, this));
		}else{
			Label gIDLab = new Label("Group ID:");
			gIDLab.addStyleName(style.boldLabel());
			infoTable.setWidget(0, 0, gIDLab);
			Label gID = new Label(""+group.getId());
			gID.addStyleName(style.label());
			infoTable.setWidget(0, 1, gID);
			
			Label gNameLab = new Label("Group Name:");
			gNameLab.addStyleName(style.boldLabel());
			infoTable.setWidget(1, 0, gNameLab);
			Label gName = new Label(group.getName());
			gName.addStyleName(style.label());
			infoTable.setWidget(1, 1, gName);
			
			Label gDescLab = new Label("Group Description:");
			gDescLab.addStyleName(style.boldLabel());
			infoTable.setWidget(2, 0, gDescLab);
			Label gDesc = new Label(group.getDescription());
			gDesc.addStyleName(style.label());
			infoTable.setWidget(2, 1, gDesc);
		}
	}

	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(this.group.getId() == group.getId() &&
				type == GroupUpdateType.GROUP){
			renderInfoTable(false);
		}
	}

}
