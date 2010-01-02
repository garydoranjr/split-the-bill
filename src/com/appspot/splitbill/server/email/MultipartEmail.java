package com.appspot.splitbill.server.email;

import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.InternetAddress;

public class MultipartEmail implements Email {

	private InternetAddress sender, rcpt;
	private String subject = "(No Subject)";
	private EmailContent 	header = new NullEmailContent(),
							footer = new NullEmailContent();
	private List<EmailContent> contents = new LinkedList<EmailContent>();
	
	@Override
	public String getHTML() {
		String retVal = header.getHTML();
		for(EmailContent content : contents){
			retVal += content.getHTML();
		}
		retVal += footer.getHTML();
		return retVal;
	}

	@Override
	public String getPlaintext() {
		String retVal = header.getPlainText();
		for(EmailContent content : contents){
			retVal += content.getPlainText();
		}
		retVal += footer.getPlainText();
		return retVal;
	}

	@Override
	public InternetAddress getRecipient() {
		return rcpt;
	}

	@Override
	public InternetAddress getSender() {
		return sender;
	}

	@Override
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}
	
	public void setSender(InternetAddress sender){
		this.sender = sender;
	}
	
	public void setRecipient(InternetAddress rcpt){
		this.rcpt = rcpt;
	}
	
	public void setHeader(EmailContent header){
		this.header = header;
	}
	
	public void setFooter(EmailContent footer){
		this.footer = footer;
	}
	
	public void addContent(EmailContent content){
		this.contents.add(content);
	}

	@Override
	public boolean hasHTML() {
		return true;
	}

}
