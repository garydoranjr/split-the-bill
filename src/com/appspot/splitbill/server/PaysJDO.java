package com.appspot.splitbill.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PaysJDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	@Persistent
	private GroupJDO group;
	@Persistent
	private Key payer;
	@Persistent
	private Key payee;
	@Persistent
	private double amount;
	@Persistent
	private String description;
	@Persistent
	private Date date;
	
	public PaysJDO(GroupJDO group,
			Key payer,
			Key payee,
			double amount,
			String description,
			Date date){
		this.group = group;
		this.payer = payer;
		this.payee = payee;
		this.amount = amount;
		this.description = description;
		this.date = date;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setGroup(GroupJDO group) {
		this.group = group;
	}

	public GroupJDO getGroup() {
		return group;
	}

	public void setPayer(Key payer) {
		this.payer = payer;
	}

	public Key getPayer() {
		return payer;
	}

	public void setPayee(Key payee) {
		this.payee = payee;
	}

	public Key getPayee() {
		return payee;
	}

	public Key getKey() {
		return key;
	}
	
	public Date getDate(){
		return date;
	}
	public void setDate(Date date){
		this.date = date;
	}
}
