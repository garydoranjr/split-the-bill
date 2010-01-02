package com.appspot.splitbill.client;

public enum EmailFrequency {
	NEVER("Never"),
	WEEKLY("Weekly");
	
	private String name;
	private EmailFrequency(String name){
		this.name = name;
	}
	public String getNiceName(){
		return name;
	}
}
