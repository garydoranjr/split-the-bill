package com.appspot.splitbill.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.appspot.splitbill.client.EmailFrequency;
import com.appspot.splitbill.client.PersonType;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersonJDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private GroupJDO group;
	
	@Persistent
	private String name;
	
	@Persistent
	private User user;
	
	@Persistent
	private double weight;
	
	@Persistent
	private PersonType type;
	
	@Persistent
	private EmailFrequency emailFreq;
	
	public PersonJDO(){}
	
	public PersonJDO(GroupJDO group,
			String name,
			User user,
			double weight,
			PersonType type,
			EmailFrequency emailFreq){
		this.group = group;
		this.name = name;
		this.user = user;
		this.weight = weight;
		this.type = type;
		this.emailFreq = emailFreq;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setType(PersonType type) {
		this.type = type;
	}

	public PersonType getType() {
		return type;
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

	public void setEmailFreq(EmailFrequency emailFreq) {
		this.emailFreq = emailFreq;
	}

	public EmailFrequency getEmailFreq() {
		return emailFreq == null ? EmailFrequency.NEVER : emailFreq;
	}
	
}
