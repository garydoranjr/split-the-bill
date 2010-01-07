package com.appspot.splitbill.server.email;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

public interface Email {
	public InternetAddress getSender() throws UnsupportedEncodingException;
	public InternetAddress getRecipient() throws UnsupportedEncodingException;
	public String getSubject();
	public String getPlaintext();
	public boolean hasHTML();
	public String getHTML();
}
