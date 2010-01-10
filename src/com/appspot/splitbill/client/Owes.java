package com.appspot.splitbill.client;

import com.appspot.splitbill.client.util.Formatting;

public class Owes {
	
	private long personID;
	private String name;
	private double amount;

	public Owes(long personID, String name, double amount){
		this.personID = personID;
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public double getAmount() {
		if(Math.abs(amount) < .01){
			return 0d;
		}else{
			return amount;
		}
	}
	
	public String getNiceAmount(){
		return Formatting.formatAmount(getAmount());
	}

	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public long getPersonID() {
		return personID;
	}
	
}
