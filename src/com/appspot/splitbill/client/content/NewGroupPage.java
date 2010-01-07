package com.appspot.splitbill.client.content;

import com.appspot.splitbill.client.GroupThumbnail;
import com.appspot.splitbill.client.event.ContentChangeEvent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.rpc.GroupManager;
import com.appspot.splitbill.client.widgets.editor.Editor;
import com.appspot.splitbill.client.widgets.editor.MinimumLengthTextBox;
import com.appspot.splitbill.client.widgets.editor.Editor.EditorChangeListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class NewGroupPage extends Composite implements EditorChangeListener{

	private static NewGroupPageUiBinder uiBinder = GWT
			.create(NewGroupPageUiBinder.class);
	interface NewGroupPageUiBinder extends UiBinder<Widget, NewGroupPage> {}
	
	private GroupManager groupManager;
	private EventBus eventBus;
	
	@UiField MinimumLengthTextBox nameBox;
	@UiField TextArea descBox;
	
	@UiField Button saveButton;
	@UiField Button cancelButton;

	public NewGroupPage(GroupManager groupManager,
			EventBus eventBus) {
		this.groupManager = groupManager;
		this.eventBus = eventBus;
		initWidget(uiBinder.createAndBindUi(this));
		nameBox.addChangeListener(this);
		setName("New Group");
	}
	
	protected void setName(String text){
		nameBox.setValue(text);
	}
	
	protected void setDesc(String text){
		descBox.setValue(text);
	}
	
	protected GroupManager getGroupManager(){
		return groupManager;
	}
	
	@UiHandler("cancelButton")
	void clickedCancel(ClickEvent e){
		doCancel();
		cancelButton.setEnabled(false);
	}
	
	@UiHandler("saveButton")
	void clickedSave(ClickEvent e){
		doSave();
		saveButton.setEnabled(false);
	}

	@Override
	public void editorChanged(Editor source) {
		saveButton.setEnabled(source.isEditOK());
	}
	
	protected void doCancel(){
		eventBus.fireEvent(new ContentChangeEvent(ContentType.WELCOME));
	}
	
	protected void doSave(){
		getGroupManager().addGroup(new GroupThumbnail(null, nameBox.getValue(), descBox.getValue(), null));
		eventBus.fireEvent(new ContentChangeEvent(ContentType.WELCOME));
	}

}
