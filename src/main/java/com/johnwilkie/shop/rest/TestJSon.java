package com.johnwilkie.shop.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.johnwilkie.shop.dto.ProductUploadModel;
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

	@GetMapping("/getProd/{id}")
	public BikeProduct getProduct(@PathVariable("id") long id) {
		return bikerepo.findById(id).orElse(null);
	}
	
	@PostMapping("/test/arrayofobject")
	@ResponseBody
	public ProductUploadModel deserialize(@RequestParam("prod") String prod,@RequestParam("file") MultipartFile file
			,@RequestParam("desc") String desc, @RequestParam("price") List<String> price, @RequestParam("stock") List<String> stock
			,@RequestParam("name") List<String> name) {
		System.out.println(file.getOriginalFilename());
		System.out.println(prod);
		System.out.println(file);
		return null;
		
	}
}
