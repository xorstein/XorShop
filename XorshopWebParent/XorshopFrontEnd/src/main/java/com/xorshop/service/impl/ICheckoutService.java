package com.xorshop.service.impl;

import java.util.List;

import com.xorshop.checkout.CheckoutInfo;
import com.xorshop.common.entity.CartItem;
import com.xorshop.common.entity.ShippingRate;

public interface ICheckoutService {
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate);
}
