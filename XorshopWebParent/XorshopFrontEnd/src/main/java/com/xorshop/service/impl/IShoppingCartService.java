package com.xorshop.service.impl;
import java.util.List;

import com.xorshop.common.entity.CartItem;
import com.xorshop.common.entity.Customer;
import com.xorshop.common.exception.ShoppingCartException;

public interface IShoppingCartService {

	public Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException;
	
	public List<CartItem> listCartItems(Customer customer);
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer);
	
	public void removeProduct(Integer productId, Customer customer);

	public void deleteByCustomer(Customer customer);
}
