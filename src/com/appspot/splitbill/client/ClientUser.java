package com.appspot.splitbill.client;

import java.io.Serializable;

public class ClientUser implements Serializable {
	
	private static final long serialVersionUID = 4733446037114135787L;
	
	private String email, nickname, id;

	public ClientUser(){}
	
	public ClientUser(String id,
			String email,
			String nickname){
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}
	
	public ClientUser(ClientUser other){
		if(other != null){
			this.email = other.email;
			this.nickname = other.nickname;
			this.id = other.id;
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object other){
		if(other != null && other instanceof ClientUser){
			ClientUser o = (ClientUser)other;
			return this.email.equals(o.email);
		}else{
			return false;
		}
	}
	
}
