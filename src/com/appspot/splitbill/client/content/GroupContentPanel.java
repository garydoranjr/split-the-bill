package com.appspot.splitbill.client.content;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.content.group.GroupContent;
import com.appspot.splitbill.client.content.group.GroupContentProvider;
import com.appspot.splitbill.client.content.group.GroupContentType;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupContentChangeEvent;
import com.appspot.splitbill.client.event.GroupSelectedEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.HistoryStateEvent;
import com.appspot.splitbill.client.event.GroupSelectedEvent.GroupSelectedListener;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateHandler;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.event.HistoryStateEvent.HistoryStateListener;
import com.appspot.splitbill.client.history.HistoryStateManager;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GroupContentPanel extends Composite implements
													GroupSelectedListener,
													SelectionHandler<Integer>,
													GroupUpdateHandler,
													HistoryStateListener{

	private HistoryStateManager historyManager;
	private EventBus eventBus;
	private GroupManager groupManager;
	private GroupContentProvider contentProvider;
	private DecoratedTabPanel panel = new DecoratedTabPanel();
	
	private VerticalPanel[] contentPanels;
	private GroupContentType[] panelTypes = GroupContentType.values();
	private GroupContent[] content;
	private Long selectedGroupID = null;
	private int selectedTab;
	
	@Inject
	public GroupContentPanel(HistoryStateManager historyManager,
			EventBus eventBus,
			GroupManager groupManager,
			GroupContentProvider contentProvider){
		this.historyManager = historyManager;
		this.eventBus = eventBus;
		this.groupManager = groupManager;
		this.contentProvider = contentProvider;
		
		eventBus.addHandler(GroupUpdateEvent.TYPE, this);
		eventBus.addHandler(GroupSelectedEvent.TYPE, this);
		eventBus.addHandler(HistoryStateEvent.TYPE, this);
		
		panel.setWidth("100%");
		panel.getDeckPanel().removeStyleName("gwt-TabPanelBottom");
		panel.getDeckPanel().addStyleName("gwt-MyTabPanelBottom");
		initWidget(panel);
		contentPanels = new VerticalPanel[panelTypes.length];
		content = new GroupContent[panelTypes.length];
		for(int i = 0; i < panelTypes.length; i++){
			contentPanels[i] = new VerticalPanel();
			contentPanels[i].setWidth("100%");
			content[i] = contentProvider.getGroupContent(panelTypes[i], null);
			contentPanels[i].add(content[i].getPanelWidget());
			panel.add(contentPanels[i], panelTypes[i].getName());
		}
		
		GroupContentType selectedType = historyManager.getGroupContentType();
		panel.selectTab(0);
		selectedTab = 0;
		for(int i = 0; i < panelTypes.length; i++){
			if(selectedType == panelTypes[i]){
				panel.selectTab(i);
				selectedTab = i;
				break;
			}
		}
		
		panel.addSelectionHandler(this);
	}

	@Override
	public void groupSelected(Long groupID) {
		selectedGroupID = groupID;
		checkSelected();
	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		selectedTab = event.getSelectedItem();
		eventBus.fireEvent(new GroupContentChangeEvent(panelTypes[selectedTab]));
		checkSelected();
	}
	
	private void checkSelected(){
		if(content[selectedTab].forGroup() != selectedGroupID){
			updatePanel(null, selectedTab);
			if(selectedGroupID != null){
				groupManager.updateGroup(selectedGroupID);
			}
		}
	}
	
	@Override
	public void groupUpdated(GroupUpdateType type, Group group, Bill bill) {
		if(type == GroupUpdateType.GROUP &&
				group.getId() == selectedGroupID &&
				!(content[selectedTab].forGroup() == selectedGroupID)){
			updatePanel(group, selectedTab);
		}
	}

	private void updatePanel(Group group, int i){
		GroupContent content = contentProvider.getGroupContent(panelTypes[i], group);
		updatePanel(i, content);
	}
	
	private void updatePanel(int i, GroupContent content){
		this.content[i].unregister();
		this.content[i] = content;
		contentPanels[i].clear();
		contentPanels[i].add(content.getPanelWidget());
	}

	@Override
	public void historyStateChanged() {
		GroupContentType type = historyManager.getGroupContentType();
		for(int i = 0; i < panelTypes.length; i++){
			if(type == panelTypes[i]){
				if(i != selectedTab){
					panel.selectTab(i);
				}
				return;
			}
		}
	}
}
