package com.appspot.splitbill.client.gin;

import com.appspot.splitbill.client.content.ContentProvider;
import com.appspot.splitbill.client.content.ContentProviderImpl;
import com.appspot.splitbill.client.content.group.GroupContentProvider;
import com.appspot.splitbill.client.content.group.GroupContentProviderImpl;
import com.google.gwt.inject.client.AbstractGinModule;

public class SplitBillModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(Object.class).annotatedWith(RootInstance.class).toProvider(SplitBillProvider.class);
		bind(ContentProvider.class).to(ContentProviderImpl.class);
		bind(GroupContentProvider.class).to(GroupContentProviderImpl.class);
	}

}
