package com.appspot.splitbill.server;

import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.LoginInfo;
import com.appspot.splitbill.client.rpc.LoginService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {

	private static final long serialVersionUID = 2480124999041934841L;

	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			loginInfo.setLoggedIn(true);
			String id = user.getUserId();
			if(id == null){
				id = user.getEmail();
			}
			loginInfo.setUserInfo(new ClientUser(id,
					user.getEmail(),
					user.getNickname()));
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

}