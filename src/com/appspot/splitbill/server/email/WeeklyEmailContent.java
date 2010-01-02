package com.appspot.splitbill.server.email;

import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.server.ServiceStatic;

public class WeeklyEmailContent implements EmailContent {

	private String plain = "", html = "";
	
	public WeeklyEmailContent(Group group, Person person){
		
		// Header
		String name = group.getName();
		String desc = group.getDescription();
		
		plain += name + "\n" + desc + "\n";
		html += "<h2>" + name + "</h2><h3>" + desc +"</h3>";
		
		// Data
		long personID = person.getID();
		double pot = group.pot();
		double spent = group.getSpent(personID);
		double paid = group.getPaid(personID);
		double owes = group.getOwes(personID).getAmount();
		String potStr = ServiceStatic.formatAmount(pot);
		String spentStr = ServiceStatic.formatAmount(spent);
		String paidStr = ServiceStatic.formatAmount(paid);
		String owesStr = ServiceStatic.formatAmount(Math.abs(owes));
		
		plain += "     - Size of Pot: " + potStr;
		plain += "     - You spent: " + spentStr;
		plain += "     - You paid: " + paidStr;
		if(owes <= -0.01){
			plain += "     - You are owed: " + owesStr;
		}else{
			plain += "     - You owe: " + owesStr;
		}
		plain += "\n\n";
		
		html += "<ul>";
		html += getLI("Size of Pot:", pot);
		html += getLI("You spent:", -spent);
		html += getLI("You paid:", paid);
		if(owes <= -0.01){
			html += getLI("You are owed: ", -owes);
		}else{
			html += getLI("You owe: ", -owes);
		}
		html += "</ul>";
		html += "<br />";
		
	}
	
	private String getLI(String label, double amount){
		String color = amount >= 0 ? "green" : "red";
		return "<li>"+label+" <b style=\"color:"+color+"\">" +
			ServiceStatic.formatAmount(Math.abs(amount)) + "</b></li>";
	}
	
	@Override
	public String getHTML() {
		return html;
	}

	@Override
	public String getPlainText() {
		return plain;
	}

}
