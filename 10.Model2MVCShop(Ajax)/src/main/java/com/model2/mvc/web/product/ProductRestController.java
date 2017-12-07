package com.model2.mvc.web.product;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


//==> 회원관리 Controller
@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	//setter Method 구현 않음
		
	public ProductRestController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping(value="json/addProduct", method=RequestMethod.POST)
	public Product addProduct( @RequestBody Product product) throws Exception {
		
		System.out.println("json/product/addProduct : POST");
		
		//File f = new File("C:\\Users\\bitcamp\\git\\10PJT\\10.Model2MVCShop(Ajax)\\WebContent\\images\\uploadFiles\\"+multi.getOriginalFilename());
		//multi.transferTo(f);
		//product.setFileName(multi.getOriginalFilename());
		
		productService.addProduct(product);
		
		return product;
	}
	
	@RequestMapping(value="json/getProduct/{prodNo}")
	public Product getProduct( @PathVariable String prodNo) throws Exception {
		
		System.out.println("json/product/getProduct : GET / POST");
		
		return productService.getProduct(Integer.parseInt(prodNo));
	}
	
	@RequestMapping(value="json/updateProduct/{prodNo}", method=RequestMethod.GET)
	public Map updateProduct( @PathVariable String prodNo , Model model ) throws Exception{

		System.out.println("json/product/updateProduct : GET");
		//Business Logic
		
		Map<String , Object> map = new HashMap();
		map.put("productVO", productService.getProduct(Integer.parseInt(prodNo)));
		map.put("purchaseVO", purchaseService.getPurchase2(Integer.parseInt(prodNo)));
		
		return map;
	}
	
	@RequestMapping(value="json/updateProduct", method=RequestMethod.POST)
	public Product updateProduct( @RequestBody Product product, Model model ) throws Exception{

		System.out.println("json/product/updateProduct : POST");
		
		//Business Logic
		productService.updateProduct(product);
		
		return product;
	}
	
	@RequestMapping(value="json/listProduct")
	public Map listProduct(  @RequestBody Search search) throws Exception{
		
		System.out.println("/product/listProduct : GET / POST");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		// Business logic 수행
		Map<String , Object> map = new HashMap();
		map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		// Model 과 View 연결
		map.put("resultPage", resultPage);
		map.put("search", search);
		
		return map;
	}
}