package com.appspot.splitbill.client.content.group;

public enum GroupContentType {
	DASHBOARD("Dashboard"),
	BILLS("Bills"),
	OWES("Owes"),
	PAYMENTS("Payments"),
	MEMBERS("Members"),
	SETTINGS("Settings");
	
	private String name;
	
	private GroupContentType(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
