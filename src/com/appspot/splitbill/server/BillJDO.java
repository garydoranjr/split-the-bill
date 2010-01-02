package com.appspot.splitbill.server;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BillJDO {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	@Persistent
	private GroupJDO group;
	@Persistent
	private Key buyer;
	@Persistent
	private Date date;
	@Persistent
	private String payee;
	@Persistent
	private String description;
	@Persistent
	private double amount;
	@Persistent(mappedBy = "order")
	private List<GetsJDO> gets;
	
	public BillJDO(){}
	
	public BillJDO(GroupJDO group,
			Key buyer,
			Date date,
			String payee,
			String description,
			double amount){
		this.group = group;
		this.buyer = buyer;
		this.date = date;
		this.payee = payee;
		this.description = description;
		this.amount = amount;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getPayee() {
		return payee;
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

	public void setBuyer(Key buyer) {
		this.buyer = buyer;
	}

	public Key getBuyer() {
		return buyer;
	}

	public void setGroup(GroupJDO group) {
		this.group = group;
	}

	public GroupJDO getGroup() {
		return group;
	}

	public Key getKey() {
		return key;
	}

	public List<GetsJDO> getGets() {
		return gets;
	}

	public void addGets(GetsJDO gets) {
		this.gets.add(gets);
	}

}
