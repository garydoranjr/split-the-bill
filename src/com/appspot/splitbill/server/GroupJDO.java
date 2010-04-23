package com.appspot.splitbill.server;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GroupJDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String description;
	
	@Persistent
	private String password;
	
	@Persistent(mappedBy = "group", defaultFetchGroup = "true")
	private List<BillJDO> bills;
	
	@Persistent(mappedBy = "group", defaultFetchGroup = "true")
	private List<PersonJDO> members;
	
	@Persistent(mappedBy = "group", defaultFetchGroup = "true")
	private List<PaysJDO> payments;

	public GroupJDO() {}

	public GroupJDO(String name, String description, String password) {
		this();
		this.name = name;
		this.description = description;
		this.password = password;
	}

	public long getId() {
		return this.id.getId();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public List<BillJDO> getBills() {
		return bills;
	}

	public List<PersonJDO> getMembers() {
		return members;
	}

	public List<PaysJDO> getPayments() {
		return payments;
	}
	
	public void addPerson(PersonJDO person){
		members.add(person);
	}
	
	public void addBill(BillJDO bill){
		bills.add(bill);
	}
	
	public void addPayment(PaysJDO pays){
		payments.add(pays);
	}
}
