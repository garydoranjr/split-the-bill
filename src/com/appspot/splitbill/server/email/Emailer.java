package com.appspot.splitbill.server.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class Emailer {

	public static boolean sendEmail(Email email){
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(email.getSender());
			msg.addRecipient(Message.RecipientType.TO, email.getRecipient());
			msg.setSubject(email.getSubject());
			msg.setText(email.getPlaintext());
			if(email.hasHTML()){
				msg.setContent(email.getHTML(), "text/html");
			}
			Transport.send(msg);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
		
	}

}
