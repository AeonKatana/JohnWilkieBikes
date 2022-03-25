package com.johnwilkie.shop.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private BikeProdVariationRepo bikerepo;
	
	@Autowired
	private BikeProdRepo bikeprodrepo;
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) { // Note this is only testing the Google Chart API so the data here is useless
		
		Map<String, Integer> graphData = new TreeMap<>();
		
		for(BikeProdVariation bp : bikerepo.findAll()) {
			graphData.put(bp.getBikeprod().getProdname(), bp.getStocks());
		}
        model.addAttribute("chartData", graphData);
        model.addAttribute("totalbike" ,bikeprodrepo.count());
		return "adminhome";
	}
	
	@GetMapping("/products")
	public String products() {
		return "adminproduct";
	}
	
	@GetMapping("/products/prodlist") // DataTable Source
	@ResponseBody
	public List<BikeProduct> getAllProducts(){ 
		return bikeprodrepo.findAll();
	}
	
	
}
