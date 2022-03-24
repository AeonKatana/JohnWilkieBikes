package com.johnwilkie.shop.service;

import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.PasswordModel;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.UserRepo;

@Service
public class LoginServiceImp implements LoginService {
  @Autowired
  private UserRepo repo;
  
  @Autowired
  private JavaMailSender sender;
  
  @Autowired
  private PasswordEncoder encoder;
  
  public User loginService(String username, char[] password) {
    return null;
  }

@Override
public boolean forgotPass(String email, HttpServletRequest request) {
	
	User user = repo.findByEmail(email);
	if(user == null) {
		return false;
	}
	user.setPasswordtoken(UUID.randomUUID().toString());
	repo.save(user);
	
	MimeMessage message = sender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message);
	
	String text = "You have requested for a password reset. Please click the link below to create a new password"
					+ "<br> <a href=\"[[URL]]\">Reset Password</a>";
	
	String url = "https://" + request.getServerName() + "/resetPass?token=" + user.getPasswordtoken();
	text = text.replace("[[URL]]", url);
	System.out.println("HELLO!??????????");
	try {
		helper.setSubject("JWBike Password Reset");
		helper.setText(text, true);
		helper.setTo(email);
		helper.setFrom("aeonkatana@gmail.com", "John Wilkie's Online Bike Shop");
		System.out.println("HELLO!??????????");
		sender.send(message);
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	
	return true;
}

@Override
public User findByPasswordToken(String token) {
	return repo.findByPasswordtoken(token);
}

@Override
public User newPassword(PasswordModel model) {
	User user = repo.findByPasswordtoken(model.getToken());
	user.setPassword(encoder.encode(model.getPassword()).toCharArray());
	user.setPasswordtoken(null);
	repo.save(user);
	return user;
}
}
