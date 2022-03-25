package com.johnwilkie.shop;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.repository.UserAddressRepo;
import com.johnwilkie.shop.repository.UserRepo;

@SpringBootTest
class JohnWilkieOnlineApplicationTests {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private UserAddressRepo addrepo;
	
	@Autowired
	private PasswordEncoder encoder;

	
	void contextLoads() {
		
		BikeProduct bp = new BikeProduct();
		bp.setProddiscout(10);
		BikeProdVariation bpv = new BikeProdVariation();
		bpv.setPrice(new BigDecimal(500));
		System.out.println("Original Price :" + bpv.getPrice());
		bpv.setBikeprod(bp);
		System.out.println("Discounted by " + bp.getProddiscout() + "% :" + bpv.getDiscountedprice());
	
	}

}
