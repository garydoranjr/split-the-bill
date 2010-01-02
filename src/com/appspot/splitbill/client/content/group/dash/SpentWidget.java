package com.appspot.splitbill.client.content.group.dash;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.appspot.splitbill.client.widgets.AmountLabel;
import com.google.gwt.user.client.ui.Widget;

public class SpentWidget extends AbstractDashboardContent {

	private LoginManager loginManager;
	private Group group;
	private AmountLabel label = new AmountLabel();
	
	public SpentWidget(LoginManager loginManager, Group group){
		this.loginManager = loginManager;
		this.group = group;
		setTitle("You spent:");
		update();
	}
	
	@Override
	public Widget getContentWidget() {
		return label;
	}

	@Override
	public boolean wantsChrome() {
		return true;
	}

	protected void update(){
		ClientUser user = loginManager.getInfo().getUserInfo();
		Person p = group.getPerson(user);
		double amount = group.getSpent(p.getID());
		label.setAmount(-amount);
	}
	
	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(group.getId() == this.group.getId()){
			this.group = group;
			update();
		}
	}

}
