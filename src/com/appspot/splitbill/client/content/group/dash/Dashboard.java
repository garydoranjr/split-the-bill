package com.appspot.splitbill.client.content.group.dash;

import java.util.ArrayList;
import java.util.List;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.content.group.GroupContent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends Composite implements GroupContent, GroupUpdateHandler{

	private static DashboardUiBinder uiBinder = GWT.create(DashboardUiBinder.class);
	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {}

	private static final int COLUMNS = 4;
	
	private Group group;
	private HandlerRegistration registration;
	
	private List<DashboardContent> content = new ArrayList<DashboardContent>();
	
	@UiField VerticalPanel panel;
	@UiField Label nameLabel;
	@UiField Label descLabel;
	@UiField FlexTable dashTable;
	
	public Dashboard(Group group, EventBus eventBus, LoginManager loginManager) {
		this.group = group;
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(group.getName());
		descLabel.setText(group.getDescription());
		dashTable.setCellPadding(5);
		eventBus.addHandler(GroupUpdateEvent.TYPE, this);
		addDashBoardContent(new PotWidget(group));
		addDashBoardContent(new SpentWidget(loginManager, group));
		addDashBoardContent(new PaidWidget(loginManager, group));
		addDashBoardContent(new OweWidget(loginManager, group));
	}

	@Override
	public Long forGroup() {
		return group.getId();
	}

	@Override
	public Widget getPanelWidget() {
		return this;
	}

	@Override
	public void unregister() {
		if(registration != null){
			registration.removeHandler();
		}
	}

	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(group.getId() == this.group.getId()){
			if(type == GroupUpdateType.GROUP){
				nameLabel.setText(group.getName());
				descLabel.setText(group.getDescription());
			}
			for(DashboardContent c : content){
				c.groupUpdated(type, group, bill);
			}
		}
	}
	
	protected void addDashBoardContent(DashboardContent dc){
		content.add(dc);
		Widget w = new ContentChrome(dc);
		int i = content.size() - 1;
		int row = i / COLUMNS;
		int column = i % COLUMNS;
		dashTable.setWidget(row, column, w);
		dashTable.getCellFormatter().setAlignment(row, column, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
//		dashTable.getColumnFormatter().setWidth(column, "100%");
	}

}
