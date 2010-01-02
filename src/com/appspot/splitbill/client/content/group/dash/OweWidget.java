package com.appspot.splitbill.client.content.group.dash;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Owes;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.appspot.splitbill.client.widgets.AmountLabel;
import com.google.gwt.user.client.ui.Widget;

public class OweWidget extends AbstractDashboardContent {

	private LoginManager loginManager;
	private Group group;
	private AmountLabel label = new AmountLabel();
	
	public OweWidget(LoginManager loginManager, Group group){
		this.loginManager = loginManager;
		this.group = group;
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

	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(group.getId() == this.group.getId()){
			this.group = group;
			update();
		}
	}
	
	protected void update(){
		ClientUser user = loginManager.getInfo().getUserInfo();
		Person p = group.getPerson(user);
		Owes owe = group.getOwes(p.getID());
		double amount = owe.getAmount();
		label.setAmount(-amount);
		if(amount <= -0.01){
			setTitle("You are owed:");
		}else{
			setTitle("You owe:");
		}
	}

}
