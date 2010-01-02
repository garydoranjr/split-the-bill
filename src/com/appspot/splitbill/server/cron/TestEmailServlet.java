package com.appspot.splitbill.server.cron;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestEmailServlet extends HttpServlet {

	private static final long serialVersionUID = 6647075270089904428L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		resp.setContentType("text/plain");
		
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Hello World";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("garydoranjr@gmail.com", "Split-the-Bill Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("gbd6@case.edu", "Gary Doran"));
            msg.setSubject("A test email from Split the Bill");
            msg.setText(msgBody);
            msg.setContent("<b>Hello World</b>", "text/html");
            Transport.send(msg);
            resp.getWriter().println("Successful");

        } catch (AddressException e) {
        	e.printStackTrace();
        	resp.getWriter().println("Address Exception");
        } catch (MessagingException e) {
        	e.printStackTrace();
        	resp.getWriter().println("Messaging Exception");
        }
		
	}

}
