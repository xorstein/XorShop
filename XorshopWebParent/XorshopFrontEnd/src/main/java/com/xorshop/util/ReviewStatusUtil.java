package com.xorshop.util;

import java.util.Iterator;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.order.Order;
import com.xorshop.common.entity.order.OrderDetail;
import com.xorshop.common.entity.product.Product;
import com.xorshop.service.ReviewService;

public class ReviewStatusUtil {

	public static void setProductReviewableStatus(Customer customer, Order order,ReviewService reviewService) {
		Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();

		while(iterator.hasNext()) {
			OrderDetail orderDetail = iterator.next();
			Product product = orderDetail.getProduct();
			Integer productId = product.getId();

			boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, productId);
			product.setReviewedByCustomer(didCustomerReviewProduct);

			if (!didCustomerReviewProduct) {
				boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, productId);
				product.setCustomerCanReview(canCustomerReviewProduct);
			}

		}
	}
}
