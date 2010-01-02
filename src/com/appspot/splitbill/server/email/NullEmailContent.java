package com.appspot.splitbill.server.email;

public class NullEmailContent implements EmailContent {

	@Override
	public String getHTML() {
		return "";
	}

	@Override
	public String getPlainText() {
		return "";
	}

}
