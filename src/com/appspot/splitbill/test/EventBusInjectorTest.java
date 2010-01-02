package com.appspot.splitbill.test;

import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.gin.SplitBillGinjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class EventBusInjectorTest extends GWTTestCase {

	public void testEventBusInjector(){
		SplitBillGinjector inject = GWT.create(SplitBillGinjector.class);
		EventBus inst1 = inject.getEventBus();
		EventBus inst2 = inject.getEventBus();
		assertEquals(inst1, inst2);
	}

	@Override
	public String getModuleName() {
		return "SplitBill";
	}

}
