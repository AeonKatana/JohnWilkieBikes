package com.johnwilkie.shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.repository.UserAddressRepo;
import com.johnwilkie.shop.repository.UserRepo;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.utils.Utils;

@SpringBootTest
class JohnWilkieOnlineApplicationTests {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private UserAddressRepo addrepo;
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private BikeProdVariationRepo bikevarrepo;
	
	@Autowired
	private BikeProdRepo bikerepo;
	
	void contextLoads() {
		
		BikeProduct bp = new BikeProduct();
		bp.setProddiscout(10);
		BikeProdVariation bpv = new BikeProdVariation();
		bpv.setPrice(new BigDecimal(500));
		System.out.println("Original Price :" + bpv.getPrice());
		bpv.setBikeprod(bp);
		System.out.println("Discounted by " + bp.getProddiscout() + "% :" + bpv.getDiscountedprice());
	
	}
	
	void testingUploadImage() throws IOException {
		
		ImageKit imageKit=ImageKit.getInstance();
	    Configuration config=Utils.getSystemConfig(JohnWilkieOnlineApplication.class);
	     imageKit.setConfig(config);
	     System.out.println("Hello World!");
	     
	     byte[] bytes= Files.readAllBytes(Paths.get("C:\\Users\\user\\Pictures\\ERD - SS.png"));
	     FileCreateRequest fileCreateRequest =new FileCreateRequest(bytes, "erd.jpg");
	     fileCreateRequest.setUseUniqueFileName(false);
	     Result result = ImageKit.getInstance().upload(fileCreateRequest);
	     
//	     Map<String , String> options=new HashMap<String,String>();
//	     options.put("skip","0");
//	     options.put("limit", "10");
//	     ResultList resultList=ImageKit.getInstance().getFileList(options);
//	     
//	     for(BaseFile result : resultList.getResults()) {
//	    	 System.out.println(result.getName());
//	     }
		
		
	}
	
	
	
	
	

}
