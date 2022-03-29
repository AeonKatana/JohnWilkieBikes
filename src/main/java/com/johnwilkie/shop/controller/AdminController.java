package com.johnwilkie.shop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.johnwilkie.shop.JohnWilkieOnlineApplication;
import com.johnwilkie.shop.dto.ProductUpdateModel;
import com.johnwilkie.shop.dto.ProductUploadModel;
import com.johnwilkie.shop.dto.VariationUpdateModel;
import com.johnwilkie.shop.model.BikeCategory;
import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.ProdCategory;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeCategoryRepo;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.repository.OrderRepo;
import com.johnwilkie.shop.repository.ProdCategoryRepo;
import com.johnwilkie.shop.repository.UserRepo;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.utils.Utils;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private BikeProdVariationRepo bikerepo;

	@Autowired
	private BikeProdRepo bikeprodrepo;

	@Autowired
	private ProdCategoryRepo prodcatrepo;

	@Autowired
	private BikeCategoryRepo bikecatrepo;
	
	@Autowired
	private UserRepo userrepo;

	@Autowired
	private OrderRepo orderrepo;
	
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) { // Note this is only testing the Google Chart API so the data here is useless

		Map<String, Integer> graphData = new TreeMap<>();

		for (BikeProduct bp : bikeprodrepo.findAll()) {
			graphData.put(bp.getProdname(), bp.getProdstock());
		}
		model.addAttribute("chartData", graphData);
		model.addAttribute("totalbike", bikeprodrepo.count());
		model.addAttribute("usercount", userrepo.count());
		model.addAttribute("totalorders", orderrepo.count());
		return "adminhome";
	}

	@GetMapping("/products")
	public String products(Model model) {
		model.addAttribute("categories", prodcatrepo.findAll());
		model.addAttribute("categcount", prodcatrepo.count());
		model.addAttribute("productcount", bikeprodrepo.count());
		model.addAttribute("restockcount", bikerepo.countByStocksLessThan(6));
		return "adminproduct";
	}
	
	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("usercount", userrepo.count());
		model.addAttribute("disablecount", userrepo.countByEnabledFalse());
		model.addAttribute("activecount", userrepo.countByEnabledTrue());
		model.addAttribute("lockedcount", userrepo.countByLockedFalse());
		return "adminusers";
	}
	
	@GetMapping("/orders")
	public String orders(Model model) {
		
		model.addAttribute("totalorders", orderrepo.count());
		model.addAttribute("monthlyorders", orderrepo.countByMonthAndYear(
				ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getMonth().name(), 
				ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getYear()));
		model.addAttribute("yearlyorders", orderrepo.countByYear(
				ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getYear()
				));
		return "adminorders";
	}
	
	// ---------------------------REST API Section---------------------------------------------

	@GetMapping("/products/prodlist") // DataTable Source
	@ResponseBody
	public List<BikeProduct> getAllProducts() {
		return bikeprodrepo.findAll();
	}
	
	@GetMapping("/users/list") // DataTable Source
	@ResponseBody
	public List<User> getAllUsers(){
		return userrepo.findAll();
	}
	@PutMapping("/user/setLock/{id}")
	@ResponseBody
	public boolean setLock(@PathVariable("id")long id,@RequestParam("check") boolean check) {
		User user = userrepo.findById(id).orElse(null);
		user.setLocked(check);
		userrepo.save(user);
		return check;
	}

	
	@GetMapping("/getUser/{id}")
	@ResponseBody
	public User getUser(@PathVariable("id") long id) {
		User user = userrepo.findById(id).orElse(null);
		return user; 
	}
	
	@GetMapping("/orders/list")
	@ResponseBody
	public List<Orders> getAllOrders(){
		return orderrepo.findAllByStatusNotAndStatusNotOrderByDatetimeDesc("CANCELLED", "REQUEST_CANCEL");
	}
	
	@GetMapping("/orders/getOrder/{id}")
	@ResponseBody
	public Orders getOrder(@PathVariable("id") long id) {
		return orderrepo.findById(id).orElse(null);
	}
	
	@PutMapping("/orders/changeStatus/{id}")
	@ResponseBody
	public String changeStatus(@PathVariable("id") long id, @RequestParam("status") String status) {
		Orders order = orderrepo.findById(id).orElse(null);
		order.setStatus(status);
		orderrepo.save(order);
		return "Success";
	}

	@GetMapping("/products/prodlist/{id}")
	@ResponseBody
	public BikeProduct getProduct(@PathVariable("id") long id) {
		return bikeprodrepo.findById(id).orElse(null);
	}

	@GetMapping("/products/product/{id}/{varid}")
	@ResponseBody
	public BikeProdVariation getVariation(@PathVariable("id") long id, @PathVariable("varid") long varid) {
		BikeProduct p = bikeprodrepo.findById(id).orElse(null);
		return bikerepo.findByBikeprodAndId(p, varid);
	}

	@GetMapping("/getCategories")
	@ResponseBody
	public List<ProdCategory> findAll() {
		return prodcatrepo.findAll();
	}

	@PostMapping("/addCateg")
	@ResponseBody
	public ProdCategory saveCateg(@RequestBody ProdCategory categ) {
		prodcatrepo.save(categ);
		return categ;
	}

	@PostMapping("/addCateg/product/{id}/{categid}")
	@ResponseBody
	public String addCategory(@PathVariable("id") long id, @PathVariable("categid") long categid) {
		BikeProduct bp = bikeprodrepo.findById(id).orElse(null);
		ProdCategory pc = prodcatrepo.findById(categid).orElse(null);
		BikeCategory bc = new BikeCategory();
		bc.setBikeprod(bp);
		bc.setCategory(pc);
		bikecatrepo.save(bc);
		return "Category Added!";
	}

	@PostMapping("/admin/updateProduct")
	@ResponseBody
	public String updateProduct(ProductUpdateModel product, VariationUpdateModel var) {

//		System.out.println("Product Id :" + product.getProdid());
//		System.out.println("Product Name :" + product.getName());
//		System.out.println("Variation Id :" + var.getVarid());
//		System.out.println("Variation Price :" + var.getPrice());
		BikeProduct bike = bikeprodrepo.findById(Long.parseLong(product.getProdid())).orElse(null);
		bike.setProdname(product.getName());
		bike.setProddesc(product.getDescription());
		bike.setProddiscout(product.getDiscount());

		bikeprodrepo.save(bike);

		BikeProdVariation variation = bikerepo.findById(Long.parseLong(var.getVarid())).orElse(null);
		variation.setPrice(var.getPrice());
		variation.setStocks(var.getStocks());

		bikerepo.save(variation);

		return "Updated!";
	}

	@PostMapping("/newProduct")
	@ResponseBody
	public ProductUploadModel deserialize(@RequestParam("prod") String prod, @RequestParam("file") MultipartFile file,
			@RequestParam("desc") String desc, @RequestParam("price") List<String> price,
			@RequestParam("stock") List<String> stock, @RequestParam("name") List<String> name) throws IOException {

		ImageKit imageKit = ImageKit.getInstance();
		Configuration config = Utils.getSystemConfig(JohnWilkieOnlineApplication.class);
		imageKit.setConfig(config);
		System.out.println("Hello World!");
		String imgname = file.getOriginalFilename().replaceAll("\\s+", "");
		byte[] bytes = file.getBytes();
		FileCreateRequest fileCreateRequest = new FileCreateRequest(bytes, imgname);
		fileCreateRequest.setUseUniqueFileName(false);
		Result result = ImageKit.getInstance().upload(fileCreateRequest);
		System.out.println("Message :" + result.getMessage());
		System.out.println("Message :" + result.isSuccessful());
		BikeProduct bp = new BikeProduct();
		bp.setProdimgurl(imgname);
		bp.setProdname(prod);
		bp.setProddesc(desc);
		bp.setDatetime(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).toLocalDateTime());
		bikeprodrepo.save(bp);
		for (int i = 0; i < name.size(); i++) {
			BikeProdVariation bpv = new BikeProdVariation();
			bpv.setName(name.get(i));
			bpv.setPrice(new BigDecimal(price.get(i)));
			bpv.setStocks(Integer.parseInt(stock.get(i)));
			bpv.setBikeprod(bp);
			bikerepo.save(bpv);
		}

		return null;
	}

	@PostMapping("/addVariation")
	@ResponseBody
	public String addVariation(@RequestParam("prodid") String prodid, @RequestParam("name") List<String> name,
			@RequestParam("price") List<String> price, @RequestParam("stock") List<String> stock) {

		BikeProduct bp = bikeprodrepo.findById(Long.parseLong(prodid)).orElse(null);
		for (int i = 0; i < name.size(); i++) {
			BikeProdVariation bpv = new BikeProdVariation();
			bpv.setBikeprod(bp);
			bpv.setName(name.get(i));
			bpv.setPrice(new BigDecimal(price.get(i)));
			bpv.setStocks(Integer.parseInt(stock.get(i)));
			bikerepo.save(bpv);
		}

		return "";
	}

	@Transactional
	@DeleteMapping("/deleteCategory/{id}")
	@ResponseBody
	public String deleteCategory(@PathVariable("id") long id) {
		prodcatrepo.deleteById(id);
		return "Deleted";
	}

	@Transactional
	@DeleteMapping("/deleteCategory/product/{id}/{categid}")
	@ResponseBody
	public String deletebikecateg(@PathVariable("id") long id, @PathVariable("categid") long categid) {
		BikeProduct bp = bikeprodrepo.findById(id).orElse(null);
		ProdCategory pc = prodcatrepo.findById(categid).orElse(null);
		bikecatrepo.deleteByBikeprodAndCategory(bp, pc);
		return "Category Removed!";
	}

}
