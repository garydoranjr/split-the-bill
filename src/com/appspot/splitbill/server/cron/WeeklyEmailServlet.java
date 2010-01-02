package com.appspot.splitbill.server.cron;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.EmailFrequency;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.server.GroupJDO;
import com.appspot.splitbill.server.ServiceStatic;
import com.appspot.splitbill.server.email.Email;
import com.appspot.splitbill.server.email.Emailer;
import com.appspot.splitbill.server.email.WeeklyEmail;
import com.appspot.splitbill.server.email.WeeklyEmailContent;

public class WeeklyEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 2505577205474305006L;

	@Override
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int sent = 0;
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Map<String, WeeklyEmail> emails = new HashMap<String, WeeklyEmail>();
			
			Query q = pm.newQuery(GroupJDO.class);
			List<GroupJDO> groups = (List<GroupJDO>) q.execute();
			for(GroupJDO groupJDO : groups){
				Group group = ServiceStatic.toClientGroup(groupJDO);
				for(Person person : group.getPeople()){
					ClientUser user = person.getUser();
					if(user != null &&
							person.getEmailFreq() == EmailFrequency.WEEKLY){
						String emailAddress = user.getEmail();
						WeeklyEmail email = emails.get(emailAddress);
						try{
							if(email == null){
								email = new WeeklyEmail(emailAddress, user.getNickname());
								emails.put(emailAddress, email);
							}
							WeeklyEmailContent content = new WeeklyEmailContent(group, person);
							email.addContent(content);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			
			for(Email email : emails.values()){
				if(Emailer.sendEmail(email)){
					sent++;
				}
			}
			
		}finally{
			pm.close();
			resp.setContentType("text/plain");
			resp.getWriter().println("Sent "+sent+" emails.");
		}
	}
	
}
