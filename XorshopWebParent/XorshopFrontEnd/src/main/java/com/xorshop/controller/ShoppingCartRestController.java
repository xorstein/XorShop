package com.xorshop.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.common.entity.Address;
import com.xorshop.common.entity.CartItem;
import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.ShippingRate;
import com.xorshop.common.exception.CustomerNotFoundException;
import com.xorshop.common.exception.ShoppingCartException;
import com.xorshop.service.AddressService;
import com.xorshop.service.CustomerService;
import com.xorshop.service.ShippingRateService;
import com.xorshop.service.ShoppingCartService;
import com.xorshop.util.AuthenticationControllerHelperUtil;

@RestController
public class ShoppingCartRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartRestController.class);
	
	private ShoppingCartService cartService;
	
	private AddressService addressService;
	
	private ShippingRateService shipService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	@Autowired
	public ShoppingCartRestController(ShoppingCartService cartService,
			CustomerService customerService,AddressService addressService,ShippingRateService shipService,
			AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
		super();
		this.cartService = cartService;
		this.addressService=addressService;
		this.shipService=shipService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
	}


	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		LOGGER.info("ShoppingCartRestController | addProductToCart is called");

		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			LOGGER.info("ShoppingCartRestController | addProductToCart | customer : " + customer.toString());
			
			Integer updatedQuantity;
			try {
				updatedQuantity = cartService.addProduct(productId, quantity, customer);
			} catch (ShoppingCartException ex) {
				// TODO Auto-generated catch block
				return ex.getMessage();
			}
			
			LOGGER.info("ShoppingCartRestController | addProductToCart | updatedQuantity : " + updatedQuantity);
			
			return updatedQuantity + " article(s) de ce produit ont été ajouté(s) à votre panier.";
		}else {
			return "Vous devez vous connecter pour ajouter ce produit au panier.";
		}
		
	}

	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
			HttpServletRequest request) {
		
		LOGGER.info("ShoppingCartRestController | removeProduct is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			
			LOGGER.info("ShoppingCartRestController | removeProduct | customer : " + customer.toString());
			
			cartService.removeProduct(productId, customer);
			
			return "Le produit a été supprimé de votre panier.";
		}else {
			return "Vous devez vous connecter pour supprimer le produit.";
		}
		
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		LOGGER.info("ShoppingCartRestController | updateQuantity is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			LOGGER.info("ShoppingCartRestController | updateQuantity | customer : " + customer.toString());
			
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			LOGGER.info("ShoppingCartRestController | updateQuantity | subtotal : " + subtotal);

			return String.valueOf(subtotal);
		}else {
			return "Vous devez vous connecter pour modifier la quantité de produit.";
		}
		
	}
	@GetMapping("/cart/total_products")
	public float viewCartTotal(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        LOGGER.info("ShoppingCartRestController | viewCartTotal is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		LOGGER.info("ShoppingCartRestController | viewCartTotal | customer : " + customer.toString());
		LOGGER.info("ShoppingCartRestController | viewCartTotal | cartItems : " + cartItems.size());

		float estimatedTotal = 0.0F;

		for (CartItem item : cartItems) {
			LOGGER.info("ShoppingCartRestController | viewCartTotal | item.getSubtotal() : " + item.getSubtotal());
			estimatedTotal += item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;

		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			usePrimaryAddressAsDefault = true;
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		return estimatedTotal;

	}
	@GetMapping("/cart/count_products")
	public long viewCartCountProduct(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        LOGGER.info("ShoppingCartRestController | viewCartCountProduct is called");
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		LOGGER.info("ShoppingCartRestController | viewCartCountProduct | customer : " + customer.toString());
		LOGGER.info("ShoppingCartRestController | viewCartCountProduct | cartItems : " + cartItems.size());
	
		return cartItems.size();

	}
}
