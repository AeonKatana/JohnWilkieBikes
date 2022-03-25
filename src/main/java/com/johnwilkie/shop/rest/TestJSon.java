package com.johnwilkie.shop.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.UserRepo;

@RestController
public class TestJSon {
	
	
	@Autowired
	private UserRepo userrepo;
	
	@Autowired
	private BikeProdRepo bikerepo;
	
	@GetMapping("/getUsers")
	public List<User> testUser() {
		return userrepo.findAll();
	}
	
	@GetMapping("/getProds")
	public List<BikeProduct> getProd(){
		return bikerepo.findAll();
	}

}
