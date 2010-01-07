package com.appspot.splitbill.client.event;

import com.appspot.splitbill.client.content.ContentType;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ContentChangeEvent extends GwtEvent<ContentChangeEvent.ContentChangeListener> {

	public static final Type<ContentChangeListener> TYPE = new Type<ContentChangeListener>();
	
	private ContentType newContent;
	public ContentChangeEvent(ContentType newContent){
		this.newContent = newContent;
	}
	
	public static interface ContentChangeListener extends EventHandler{
		public void contentChanged(ContentType newContent);
	}

	@Override
	protected void dispatch(ContentChangeListener handler) {
		handler.contentChanged(newContent);
	}

	@Override
	public Type<ContentChangeListener> getAssociatedType() {
		return TYPE;
	}

}
