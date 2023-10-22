package com.xorshop.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.xorshop.admin.order.OrderService;
import com.xorshop.admin.product.ProductService;
import com.xorshop.admin.setting.SettingService;
import com.xorshop.admin.util.OrderUtil;
import com.xorshop.common.entity.order.Order;
import com.xorshop.common.entity.product.Product;

import jakarta.servlet.http.HttpServletRequest;

import com.xorshop.admin.dashboard.DashboardInfo;
import com.xorshop.admin.dashboard.DashboardService;
@Controller
public class MainController {
	@Autowired 
	private ProductService productService;
	@Autowired 
	private OrderService orderService;
	@Autowired 
	private SettingService settingService;
	@Autowired 
	private DashboardService dashboardService;
	
	@GetMapping("")
	public String viewHomePage(HttpServletRequest request,Model model) {
		List<Product> listTopRaitingProduct = productService.getTopProductRating();
		List<Order> listOrders = orderService.findLastOrdersDesc();
		model.addAttribute("listTopRaitingProduct", listTopRaitingProduct);
		model.addAttribute("listOrders", listOrders);
		OrderUtil.loadCurrencySetting(request, settingService);
		DashboardInfo summary = dashboardService.loadSummary();
		model.addAttribute("summary", summary);	
		return "index";
	}
	@GetMapping("/login")
	public String viewLoginPage(Authentication authentification) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}

		return "redirect:/";
	}

}
