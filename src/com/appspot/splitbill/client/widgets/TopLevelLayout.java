package com.appspot.splitbill.client.widgets;

import com.appspot.splitbill.client.LoginInfo;
import com.appspot.splitbill.client.content.ContentProvider;
import com.appspot.splitbill.client.content.ContentType;
import com.appspot.splitbill.client.event.ContentChangeEvent;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupSelectedEvent;
import com.appspot.splitbill.client.event.HistoryStateEvent;
import com.appspot.splitbill.client.event.ContentChangeEvent.ContentChangeListener;
import com.appspot.splitbill.client.event.GroupSelectedEvent.GroupSelectedListener;
import com.appspot.splitbill.client.event.HistoryStateEvent.HistoryStateListener;
import com.appspot.splitbill.client.history.HistoryStateManager;
import com.appspot.splitbill.client.rpc.LoginManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TopLevelLayout extends Composite implements ContentChangeListener,
															GroupSelectedListener,
															HistoryStateListener{

	private static TopLevelLayoutUiBinder uiBinder = 
		GWT.create(TopLevelLayoutUiBinder.class);
	interface TopLevelLayoutUiBinder extends UiBinder<Widget, TopLevelLayout> {}
	
	private HistoryStateManager historyManager;
	private LoginManager loginManager;
	private EventBus eventBus;
	
	private GroupSelector groupSelector;
	private NotificationArea notificationArea;
	private ContentProvider contentProvider;
	
	@UiField HorizontalPanel headerPanel;
	@UiField HorizontalPanel linksArea;
	@UiField Image logo;
	
	@UiField HorizontalPanel contentRow;
	@UiField VerticalPanel contentArea;

	@Inject
	public TopLevelLayout(HistoryStateManager historyManager,
			LoginManager loginManager,
			EventBus eventBus,
			NotificationArea notificationArea,
			GroupSelector groupSelector,
			ContentProvider contentProvider) {
		this.historyManager = historyManager;
		this.loginManager = loginManager;
		this.eventBus = eventBus;
		this.groupSelector = groupSelector;
		this.notificationArea = notificationArea;
		this.contentProvider = contentProvider;
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus.addHandler(GroupSelectedEvent.TYPE, this);
		this.eventBus.addHandler(ContentChangeEvent.TYPE, this);
		this.eventBus.addHandler(HistoryStateEvent.TYPE, this);
		
		LoginInfo info = this.loginManager.getInfo();
		linksArea.add(new HTML("<b>"+info.getUserInfo().getNickname()+"</b>"));
		linksArea.add(new HTML("|"));
		linksArea.add(new HTML("<a href=\""+info.getLogoutUrl()+"\">Logout</a>"));
		linksArea.setSpacing(5);
		
		headerPanel.setCellWidth(notificationArea, "100%");
		headerPanel.setCellVerticalAlignment(logo, HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.setCellHorizontalAlignment(notificationArea, HasHorizontalAlignment.ALIGN_CENTER);
		headerPanel.setCellVerticalAlignment(linksArea, HasVerticalAlignment.ALIGN_MIDDLE);
		
		contentRow.setSpacing(10);
		contentRow.setCellWidth(contentArea, "100%");
		setContent(contentProvider.get(historyManager.getContentType()));
	}
	
	private void setContent(Widget w){
		contentArea.clear();
		contentArea.add(w);
	}
	
	@UiFactory GroupSelector getGroupSelector(){
		return groupSelector;
	}
	
	@UiFactory NotificationArea getNotificationArea(){
		return notificationArea;
	}

	private ContentType cType = ContentType.WELCOME;
	
	@Override
	public void contentChanged(ContentType newContent) {
		if(newContent != cType){
			this.cType = newContent;
			setContent(contentProvider.get(newContent));
		}
	}

	@Override
	public void groupSelected(Long groupID) {
		ContentType type = groupID == null ? ContentType.WELCOME : ContentType.GROUP;
		eventBus.fireEvent(new ContentChangeEvent(type));
	}

	@Override
	public void historyStateChanged() {
		contentChanged(historyManager.getContentType());
	}

}
