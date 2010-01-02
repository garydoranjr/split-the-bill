package com.appspot.splitbill.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GetsJDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	@Persistent
	private BillJDO order;
	@Persistent
	private Key person;
	@Persistent
	private double amount;
	@Persistent
	private String description;
	
	public GetsJDO(BillJDO order,
			Key person,
			double amount,
			String description){
		this.order = order;
		this.person = person;
		this.amount = amount;
		this.description = description;
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
	public void setOrder(BillJDO order) {
		this.order = order;
	}
	public BillJDO getOrder() {
		return order;
	}
	public void setPerson(Key person) {
		this.person = person;
	}
	public Key getPerson() {
		return person;
	}
	public Key getKey() {
		return key;
	}
}
