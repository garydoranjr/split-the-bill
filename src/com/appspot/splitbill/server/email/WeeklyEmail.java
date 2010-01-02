package com.appspot.splitbill.server.email;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

public class WeeklyEmail extends MultipartEmail {

	public WeeklyEmail(String rcptEmail, String rcptName) throws UnsupportedEncodingException{
		setRecipient(new InternetAddress(rcptEmail, rcptName));
		setSender(new InternetAddress("garydoranjr@gmail.com", "Split the Bill"));
		setSubject("Your weekly update from Split the Bill");
		setFooter(new EmailContent(){
			@Override
			public String getHTML() {
				return "<hr />"+
				"<i>To stop receiving updates, please visit "+
				"<a href=\"split-bill.appspot.com\">split-bill.appspot.com</a>.</i>";
			}
			@Override
			public String getPlainText() {
				return "To stop receiving updates, please visit split-bill.appspot.com.";
			}
		});
	}
	
}
