package com.appspot.splitbill.client.content.group.dash;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.widgets.AmountLabel;
import com.google.gwt.user.client.ui.Widget;

public class PotWidget extends AbstractDashboardContent {

	private Group group;
	private AmountLabel label;
	
	public PotWidget(Group group){
		this.group = group;
		setTitle("Size of Pot");
		label = new AmountLabel();
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
		label.setAmount(group.pot());
	}

	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(group.getId() == this.group.getId()){
			this.group = group;
			update();
		}
	}

}
