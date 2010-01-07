package com.appspot.splitbill.client.util;

import com.google.gwt.i18n.client.NumberFormat;

public class Formatting {

	public static String formatAmount(double amount){
		NumberFormat format = NumberFormat.getCurrencyFormat();
		return format.format(amount).replaceAll("US", "");
	}
	
}
