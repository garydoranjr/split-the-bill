package com.appspot.splitbill.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class Formatting {

	public static String formatAmount(double amount){
		NumberFormat format = NumberFormat.getCurrencyFormat();
		return format.format(amount).replaceAll("US", "");
	}
	
	public static String formatDate(Date date){
		if(date == null){
			return "";
		}
		return DateTimeFormat.getMediumDateFormat().format(date);
	}
	
}
