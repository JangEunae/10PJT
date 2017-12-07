package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 Controller
@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	//setter Method 구현 않음
		
	public PurchaseRestController(){
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
	
	
	
	@RequestMapping(value="json/addPurchase/{prodNo}", method=RequestMethod.GET)
	public Map addPurchase( @PathVariable String prodNo, @PathVariable String userId  ) throws Exception {
		
		System.out.println("json/purchase/addPurchase : GET");
		
		Product productVO = productService.getProduct(Integer.parseInt(prodNo));
		User userVO = userService.getUser(userId);
		
		Map map = new HashMap();
		map.put("productVO", productVO);
		map.put("userVO", userVO);
		
		return map;
	}
	
	
	@RequestMapping(value="json/addPurchase", method=RequestMethod.POST)
	public Purchase addPurchase( @RequestBody Purchase purchaseVO, @PathVariable String prodNo, @PathVariable String userId  ) throws Exception {
		
		System.out.println("json/purchase/addPurchase : POST");
		
		purchaseService.addPurchase(purchaseVO);
		
		return purchaseVO;
	}
	
	@RequestMapping(value="json/getPurchase/{tranNo}")
	public Purchase getPurchase( @RequestBody Purchase purchaseVO, @PathVariable String tranNo  ) throws Exception {
		
		System.out.println("json/purchase/getPurchase : GET / POST");
		
		return purchaseService.getPurchase(Integer.parseInt(tranNo));
	}
	
	
	@RequestMapping(value="json/updatePurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase updatePurchase( @PathVariable String tranNo ) throws Exception{

		System.out.println("json/purchase/updatePurchase : GET");
	
		return  purchaseService.getPurchase(Integer.parseInt(tranNo));
	}
	
	@RequestMapping(value="json/updatePurchase", method=RequestMethod.POST)
	public Purchase updatePurchase( @RequestBody Purchase purchase ) throws Exception{

		System.out.println("json/purchase/updatePurchase : POST");
		
		purchaseService.updatePurchase(purchase);

		return  purchase;
	}
	
	@RequestMapping(value="json/listPurchase")
	public Map listPurchase( @RequestBody Search search, HttpSession session) throws Exception{
		
		System.out.println("json/purchase/listPurchase : GET / POST");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		// Business logic 수행
		String buyerId=((User)session.getAttribute("user")).getUserId();
		Map<String , Object> map = purchaseService.getPurchaseList(search, buyerId);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		map.put("resultPage", resultPage);
		map.put("search", search);
		
		return map;
	}
	
	@RequestMapping(value="json/deletePurchase/{tranNo}", method=RequestMethod.GET )
	public Purchase deletePurchase( @PathVariable String tranNo ) throws Exception{

		System.out.println("json/purchase/deletePurchase : GET");
		
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		purchaseService.deleteTranCode(purchaseVO);
		
		//model.addAttribute("purchaseVO", purchaseVO);
		purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		return purchaseVO;
	}
	
	@RequestMapping(value="json/updateTranCode/{tranNo}",  method=RequestMethod.GET)
	public Purchase updateTranCodeAction( @PathVariable String tranNo,Model model ) throws Exception{

		System.out.println("json/purchase/updateTranCode : GET");
		
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		purchaseService.updateTranCode(purchaseVO);
		//model.addAttribute("purchaseVO", purchaseVO);
		purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		return purchaseVO;
	}
	
	@RequestMapping(value="json/updateTranCodeByProd/{prodNo}", method=RequestMethod.GET)
	public Purchase updateTranCodeByProdAction( @PathVariable String prodNo) throws Exception{

		System.out.println("json/purchase/updateTranCodeByProd");
		
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase2(Integer.parseInt(prodNo));
		purchaseService.updateTranCode(purchaseVO);
		purchaseVO = purchaseService.getPurchase2(Integer.parseInt(prodNo));
		
		return purchaseVO;
	}
	
}