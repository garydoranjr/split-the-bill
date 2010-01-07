package com.appspot.splitbill.client.widgets;

import com.appspot.splitbill.client.GroupThumbnail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GroupLabel extends Composite {

	private static GroupLabelUiBinder uiBinder = GWT
			.create(GroupLabelUiBinder.class);
	interface GroupLabelUiBinder extends UiBinder<Widget, GroupLabel> {
	}
	
	interface MyStyle extends CssResource {
		String mouseover();
		String selected();
	}
	@UiField MyStyle style;
	
	@UiField Label label;
	
	private GroupSelector parent;
	private GroupThumbnail group;
	
	public GroupLabel(GroupSelector parent, GroupThumbnail group) {
		this.parent = parent;
		this.group = group;
		initWidget(uiBinder.createAndBindUi(this));
		label.setText(group.getName());
		label.setTitle(group.getDescription());
	}
	
	@UiHandler("label")
	void onClick(ClickEvent event) {
		parent.select(this);
	}
	
	@UiHandler("label")
	void onMouseOut(MouseOutEvent event) {
		label.removeStyleName(style.mouseover());
	}
	
	@UiHandler("label")
	void onMouseOver(MouseOverEvent event) {
		label.addStyleName(style.mouseover());
	}
	
	public long getGroupID(){
		return group.getId();
	}
	
	public void select(){
		label.addStyleName(style.selected());
	}
	
	public void deselect(){
		label.removeStyleName(style.selected());
	}

}
