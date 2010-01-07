package com.appspot.splitbill.client;

import java.io.Serializable;

public class GroupThumbnail implements Serializable{

	private static final long serialVersionUID = -8049696448286603516L;
	
	private Long id;
	private String name;
	private String description;
	private String password;

	public GroupThumbnail(){}

	public GroupThumbnail(Long id,
			String name,
			String description,
			String password) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.password = password;
	}

	public Long getId() {
		return this.id;
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

}
