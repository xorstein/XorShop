package com.xorshop.admin.order;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xorshop.admin.country.CountryRepository;
import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.order.Order;
import com.xorshop.common.entity.order.OrderStatus;
import com.xorshop.common.entity.order.OrderTrack;
import com.xorshop.common.exception.OrderNotFoundException;



@Service
public class OrderService implements IOrderService{

	private static final int ORDERS_PER_PAGE = 4;

	private OrderRepository orderRepo;
	
	private CountryRepository countryRepo;
	
	
	public OrderService(OrderRepository orderRepo, CountryRepository countryRepo) {
		super();
		this.orderRepo = orderRepo;
		this.countryRepo = countryRepo;
	}

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();

		Sort sort = null;

		if ("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		} else {
			sort = Sort.by(sortField);
		}

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

		Page<Order> page = null;

		if (keyword != null) {
			page = orderRepo.findAll(keyword, pageable);
		} else {
			page = orderRepo.findAll(pageable);
		}

		helper.updateModelAttributes(pageNum, page);		
	}
	
	@Override
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return orderRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Impossible de trouver la commande avec ID " + id);
		}
	}
	
	@Override
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = orderRepo.countById(id);
		if (count == null || count == 0) {
			throw new OrderNotFoundException("Impossible de trouver la commande avec ID " + id); 
		}

		orderRepo.deleteById(id);
	}
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void save(Order orderInForm) {
		Order orderInDB = orderRepo.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDB.getOrderTime());
		orderInForm.setCustomer(orderInDB.getCustomer());

		orderRepo.save(orderInForm);
	}
	
	public void updateStatus(Integer orderId, String status) {
		Order orderInDB = orderRepo.findById(orderId).get();
		OrderStatus statusToUpdate = OrderStatus.valueOf(status);

		if (!orderInDB.hasStatus(statusToUpdate)) {
			List<OrderTrack> orderTracks = orderInDB.getOrderTracks();

			OrderTrack track = new OrderTrack();
			track.setOrder(orderInDB);
			track.setStatus(statusToUpdate);
			track.setUpdatedTime(new Date());
			track.setNotes(statusToUpdate.defaultDescription());

			orderTracks.add(track);

			orderInDB.setStatus(statusToUpdate);

			orderRepo.save(orderInDB);
		}

	}

	public List<Order> findLastOrdersDesc() {
		// TODO Auto-generated method stub
		return orderRepo.find10LastOrdersDesc();
	}
}
