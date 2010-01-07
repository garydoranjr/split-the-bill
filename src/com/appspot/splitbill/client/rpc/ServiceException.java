package com.appspot.splitbill.client.rpc;

import java.io.Serializable;

public class ServiceException extends Exception implements Serializable {

	private static final long serialVersionUID = 1384376888260453526L;

	public static enum ExceptionType {
		NOT_LOGGED_IN("Not logged in."),
		UNAUTHORIZED("Not authorized to perform request.");
		
		private String message;
		
		private ExceptionType(String message){
			this.message = message;
		}
		
		public String getMessage(){
			return message;
		}
	}
	
	private ExceptionType type;

	public ServiceException() {
		super();
	}

	public ServiceException(ExceptionType type) {
		super(type.getMessage());
		this.type = type;
	}
	
	public ExceptionType getType(){
		return type;
	}

}