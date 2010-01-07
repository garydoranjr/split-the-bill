package com.appspot.splitbill.client.history;

import com.appspot.splitbill.client.content.ContentType;
import com.appspot.splitbill.client.content.group.GroupContentType;
import com.appspot.splitbill.client.event.ContentChangeEvent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupContentChangeEvent;
import com.appspot.splitbill.client.event.GroupSelectedEvent;
import com.appspot.splitbill.client.event.HistoryStateEvent;
import com.appspot.splitbill.client.event.ContentChangeEvent.ContentChangeListener;
import com.appspot.splitbill.client.event.GroupContentChangeEvent.GroupContentChangeListener;
import com.appspot.splitbill.client.event.GroupSelectedEvent.GroupSelectedListener;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HistoryStateManager implements ValueChangeHandler<String>,
											GroupSelectedListener,
											ContentChangeListener,
											GroupContentChangeListener {
	
	private final EventBus eventBus;
	private ContentType cType = ContentType.WELCOME;
	private GroupContentType gType = GroupContentType.DASHBOARD;
	private Long group = null;
	
	@Inject
	public HistoryStateManager(EventBus eventBus){
		this.eventBus = eventBus;
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
		eventBus.addHandler(GroupSelectedEvent.TYPE, this);
		eventBus.addHandler(GroupContentChangeEvent.TYPE, this);
		eventBus.addHandler(ContentChangeEvent.TYPE, this);
	}
	
	public ContentType getContentType(){
		return cType;
	}
	
	public GroupContentType getGroupContentType(){
		return gType;
	}
	
	public Long getSelectedGroupID(){
		if(cType == ContentType.GROUP){
			return group;
		}else{
			return null;
		}
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		HistoryState state = HistoryState.parseToken(token);
		cType = ContentType.WELCOME;
		gType = GroupContentType.DASHBOARD;
		group = null;
		try{
			cType = ContentType.valueOf(state.getValue("c"));
		}catch(Throwable t){}
		try{
			gType = GroupContentType.valueOf(state.getValue("t"));
		}catch(Throwable t){}
		try{
			group = Long.parseLong(state.getValue("g"));
		}catch(Throwable t){}
		eventBus.fireEvent(new HistoryStateEvent());
	}

	@Override
	public void groupSelected(Long groupID) {
		if(this.group != groupID){
			this.group = groupID;
			if(cType == ContentType.GROUP){
				commitHistoryState();
			}
		}
	}

	@Override
	public void contentChanged(ContentType newContent) {
		if(newContent != cType && newContent != null){
			cType = newContent;
			commitHistoryState();
		}
	}

	@Override
	public void groupContentChanged(GroupContentType type) {
		if(type != gType && type != null){
			gType = type;
			commitHistoryState();
		}
	}
	
	private void commitHistoryState(){
		HistoryState state = new HistoryState("s");
		state.setValue("c", cType.name());
		if(this.group != null){
			state.setValue("g", ""+this.group);
		}
		state.setValue("t", gType.name());
		History.newItem(state.toToken(), false);
	}

}
