package com.appspot.splitbill.client.gin;

import com.appspot.splitbill.client.SplitBill;
import com.google.inject.Provider;

public class SplitBillProvider implements Provider<SplitBill> {

	@Override
	public SplitBill get() {
		return SplitBill.get();
	}

}
