package com.johnwilkie.shop.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.model.UserAddress;
import com.johnwilkie.shop.repository.UserAddressRepo;
import com.johnwilkie.shop.repository.UserRepo;

@Service
public class RegisterServiceImp implements RegisterService {
  @Autowired
  private UserRepo userrepo;
  
  @Autowired
  private UserAddressRepo addressrepo;
  
  @Autowired
  private PasswordEncoder encoder;
  
  @Autowired
  private JavaMailSender sender;
  
  @Override
  public boolean register(User user, UserAddress address, HttpServletRequest request) {
    user.setPassword(this.encoder.encode(String.valueOf(user.getPassword())).toCharArray());
    String checkusername = this.userrepo.findByUserEmail(user.getEmail());
    System.out.println(checkusername);
    System.out.println(user.getEmail());
    if (checkusername == null) {
      user.setVerifytoken(UUID.randomUUID().toString());
      user.setRole("CUSTOMER");
      user.setDatejoined(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).toLocalDateTime());
      User newuser = (User)this.userrepo.save(user);
      address.setUser(newuser);
      this.addressrepo.save(address);
      
      
      MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);
      
      String text = "Thank you for registering to John Wilkie's Online Bike Shop."
    		  		+ " You can verify your account on the link below"
    		  		+ "<h3><a href=\"[[URL]]\" target=\"self\">Verify my account</a></h3>"
    		  		+ "Thank you and happy shopping! <br>"
    		  		+ "John Wilkie's Online Bike Shop.";
      
      String verifyURL = "http://" + request.getServerName() + ":" + request.getServerPort() +"/verify?token=" + newuser.getVerifytoken();
      text = text.replace("[[URL]]", verifyURL);
      try {
    	 helper.setFrom("aeonkatana@gmail.com", "John Wilkie's Online Bike Shop");
    	 helper.setSubject("JWBikes Verify Registration");
    	 helper.setTo(user.getEmail());
    	 helper.setText(text, true);
    	 sender.send(message);
      }catch(Exception e) {
    	  
      }
      
      
      
      
      return true;
    } 
    return false;
  }
}


