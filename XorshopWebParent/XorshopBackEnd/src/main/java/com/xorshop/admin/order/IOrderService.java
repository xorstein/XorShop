package com.xorshop.admin.order;

import com.xorshop.common.exception.OrderNotFoundException;
import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.order.Order;


public interface IOrderService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);

	public Order get(Integer id) throws OrderNotFoundException;

	public void delete(Integer id) throws OrderNotFoundException;
}
