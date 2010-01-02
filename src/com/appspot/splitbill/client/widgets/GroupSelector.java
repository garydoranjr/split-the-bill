package com.appspot.splitbill.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.appspot.splitbill.client.GroupThumbnail;
import com.appspot.splitbill.client.content.ContentType;
import com.appspot.splitbill.client.event.ContentChangeEvent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupSelectedEvent;
import com.appspot.splitbill.client.event.GroupThumbsUpdateEvent;
import com.appspot.splitbill.client.event.HistoryStateEvent;
import com.appspot.splitbill.client.event.ContentChangeEvent.ContentChangeListener;
import com.appspot.splitbill.client.event.GroupThumbsUpdateEvent.GroupThumbsUpdateHandler;
import com.appspot.splitbill.client.event.HistoryStateEvent.HistoryStateListener;
import com.appspot.splitbill.client.history.HistoryStateManager;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class GroupSelector extends Composite implements GroupThumbsUpdateHandler,
														HistoryStateListener,
														ContentChangeListener{

	private static GroupSelectorUiBinder uiBinder = GWT.create(GroupSelectorUiBinder.class);
	interface GroupSelectorUiBinder extends UiBinder<Widget, GroupSelector> {}

	@UiField Button newGroupButton;
	@UiField VerticalPanel table;
	
	private EventBus eventBus;
	private HistoryStateManager historyManager;
	private GroupManager groupManager;
	private List<GroupLabel> labels = new ArrayList<GroupLabel>();
	
	@Inject
	public GroupSelector(EventBus eventBus,
			HistoryStateManager historyManager,
			GroupManager groupManager) {
		this.eventBus = eventBus;
		this.historyManager = historyManager;
		this.groupManager = groupManager;
		eventBus.addHandler(GroupThumbsUpdateEvent.TYPE, this);
		eventBus.addHandler(HistoryStateEvent.TYPE, this);
		eventBus.addHandler(ContentChangeEvent.TYPE, this);
		initWidget(uiBinder.createAndBindUi(this));
		groupManager.updateThumbs();
	}
	
	@UiHandler("newGroupButton")
	void handleClick(ClickEvent e){
		eventBus.fireEvent(new ContentChangeEvent(ContentType.CREATE));
	}
	
	private List<GroupThumbnail> groups = new ArrayList<GroupThumbnail>();
	
	public void setGroups(List<GroupThumbnail> groups){
		this.groups.clear();
		if(groups != null){
			this.groups.addAll(groups);
		}
		render();
	}

	private void render() {
		table.clear();
		labels.clear();
		Long selectedID = historyManager.getSelectedGroupID();
		GroupLabel toSelect = null;
		if(groups.size() > 0){
			for(GroupThumbnail group : groups){
				GroupLabel label = new GroupLabel(this, group);
				if(toSelect == null){
					toSelect = label;
				}
				if(selectedID != null && selectedID.equals(group.getId())){
					toSelect = label;
				}
				table.add(label);
				labels.add(label);
			}
		}
		if(historyManager.getContentType() == ContentType.GROUP){
			select(toSelect, true);
		}else{
			select(null, false);
		}
	}
	
	private GroupLabel selectedLabel = null;
	
	public void select(GroupLabel label){
		select(label, true);
	}
	
	public void select(GroupLabel label, boolean fireListeners){
		if(selectedLabel != null){
			selectedLabel.deselect();
		}
		selectedLabel = label;
		if(selectedLabel != null){
			selectedLabel.select();
		}
		
		// Fire Event
		if(fireListeners){
			GroupSelectedEvent selectionEvent;
			if(selectedLabel == null){
				selectionEvent = new GroupSelectedEvent();
			}else{
				selectionEvent = new GroupSelectedEvent(selectedLabel.getGroupID());
			}
			eventBus.fireEvent(selectionEvent);
		}
	}

	@Override
	public void groupThumbsUpdated() {
		setGroups(groupManager.getGroupThumbs());
	}

	@Override
	public void historyStateChanged() {
		if(historyManager.getContentType() == ContentType.GROUP){
			Long groupID = historyManager.getSelectedGroupID();
			GroupLabel toSelect = null;
			for(GroupLabel label : labels){
				if(toSelect == null){
					toSelect = label;
				}
				if(label.getGroupID() == groupID){
					toSelect = label;
					break;
				}
			}
			select(toSelect, true);
		}else{
			select(null, false);
		}
	}

	@Override
	public void contentChanged(ContentType newContent) {
		if(newContent != ContentType.GROUP){
			select(null, false);
		}
	}

}
