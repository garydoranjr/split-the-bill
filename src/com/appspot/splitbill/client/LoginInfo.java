package com.appspot.splitbill.client;

import java.io.Serializable;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = -2911718674880662283L;
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private ClientUser userInfo;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public void setUserInfo(ClientUser userInfo) {
		this.userInfo = userInfo;
	}

	public ClientUser getUserInfo() {
		return userInfo;
	}
	
	public boolean isUserLoggedIn(ClientUser user){
		return user != null && user.equals(userInfo);
	}
}