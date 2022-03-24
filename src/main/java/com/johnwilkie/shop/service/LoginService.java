package com.johnwilkie.shop.service;

import javax.servlet.http.HttpServletRequest;

import com.johnwilkie.shop.model.PasswordModel;
import com.johnwilkie.shop.model.User;

public interface LoginService {
	  User loginService(String paramString, char[] paramArrayOfchar);
	  
	  boolean forgotPass(String email, HttpServletRequest request);
	  User findByPasswordToken(String token);
	  User newPassword(PasswordModel model);
}
