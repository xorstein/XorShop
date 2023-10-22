package com.xorshop.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xorshop.checkout.CheckoutInfo;
import com.xorshop.common.entity.CartItem;
import com.xorshop.common.entity.ShippingRate;
import com.xorshop.service.impl.ICheckoutService;
import com.xorshop.util.CheckoutUtil;

@Service
@Transactional
public class CheckoutService implements ICheckoutService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutService.class);
	
	private static final int DIM_DIVISOR = 139;

	@Override
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
		
		LOGGER.info("CheckoutService | prepareCheckout is called");
		
		// TODO Auto-generated method stub
		CheckoutInfo checkoutInfo = new CheckoutInfo();

		float productCost = CheckoutUtil.calculateProductCost(cartItems);
		float productTotal = CheckoutUtil.calculateProductTotal(cartItems);
		float shippingCostTotal = CheckoutUtil.calculateShippingCost(cartItems, shippingRate, DIM_DIVISOR);
		float paymentTotal = productTotal + shippingCostTotal;
		
		LOGGER.info("CustomerController | prepareCheckout | productCost : " + productCost);
		LOGGER.info("CustomerController | prepareCheckout | productTotal : " + productTotal);
		LOGGER.info("CustomerController | prepareCheckout | shippingCostTotal : " + shippingCostTotal);
		LOGGER.info("CustomerController | prepareCheckout | paymentTotal : " + paymentTotal);

		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);

		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		
		LOGGER.info("CustomerController | prepareCheckout | checkoutInfo : " + checkoutInfo.toString());

		return checkoutInfo;
	}
}
