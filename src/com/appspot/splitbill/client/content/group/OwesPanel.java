package com.appspot.splitbill.client.content.group;

import java.util.List;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Owes;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;


public class OwesPanel extends Composite implements GroupUpdateHandler, GroupContent {

	private static OwesPanelUiBinder uiBinder = GWT.create(OwesPanelUiBinder.class);
	interface OwesPanelUiBinder extends UiBinder<Widget, OwesPanel> {}
	
	private EventBus eventBus;
	private HandlerRegistration registration;
	
	@UiField Image owesChart;
	
	private Group group;

	public OwesPanel(EventBus eventBus, Group group) {
		this.eventBus = eventBus;
		this.group = group;
		registration = this.eventBus.addHandler(GroupUpdateEvent.TYPE, this);
		initWidget(uiBinder.createAndBindUi(this));
		updateChart();
	}

	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(this.group.getId() == group.getId()){
			updateChart();
		}
	}
	
	private void updateChart(){
		List<Owes> owes = group.getOwes();
		String url = "http://chart.apis.google.com/chart?cht=bhs&chxt=y&chm=N*cUSD2*,000000,0,-1,11";
		String data = "&chd=t:";
		String colors = "&chco=";
		String labels = "";
		int n = 0;
		double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
		for(Owes owe : owes){
			double a = owe.getAmount();
			if(true){
				if(a < min){
					min = a;
				}
				if(a > max){
					max = a;
				}
				n++;
				data += owe.getAmount()+",";
				labels = "|"+owe.getName()+labels;
				colors += (a < 0f) ? "00FF00|" : "FF0000|";
			}
		}
		String range = Math.floor(min)+","+Math.ceil(max);
		url += "&chds="+range;
		url += "&chxr=0,"+range;
		url += "&chs=300x"+(n*30+5);
		data = data.substring(0,data.length()-1);
		colors = colors.substring(0,colors.length()-1);
		url = url + data+ colors + "&chxl=0:"+labels+"|";
		owesChart.setUrl(url);
		owesChart.setVisible(false);
		owesChart.setVisible(true);
	}
	
	@Override
	public Widget getPanelWidget(){
		return this;
	}

	@Override
	public Long forGroup() {
		return group.getId();
	}

	@Override
	public void unregister() {
		registration.removeHandler();
	}

}
