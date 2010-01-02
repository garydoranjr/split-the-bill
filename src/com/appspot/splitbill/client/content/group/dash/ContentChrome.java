package com.appspot.splitbill.client.content.group.dash;

import com.appspot.splitbill.client.content.group.dash.DashboardContent.TitleChangeListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentChrome extends Composite {

	private static ContentChromeUiBinder uiBinder = GWT.create(ContentChromeUiBinder.class);
	interface ContentChromeUiBinder extends UiBinder<Widget, ContentChrome> {}

	@UiField VerticalPanel panel;
	@UiField Label title;
	
	public ContentChrome(DashboardContent content) {
		if(content.wantsChrome()){
			initWidget(uiBinder.createAndBindUi(this));
			title.setText(content.getTitle());
			content.addTitleChangeListener(new TitleChangeListener(){
				@Override
				public void titleChanged(String newTitle) {
					title.setText(newTitle);
				}
			});
			Widget w = content.getContentWidget();
			panel.add(w);
			panel.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
		}else{
			initWidget(content.getContentWidget());
		}
	}

}
