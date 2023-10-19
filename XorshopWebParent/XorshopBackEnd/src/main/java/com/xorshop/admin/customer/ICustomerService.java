package com.xorshop.admin.customer;
import java.util.List;

import org.springframework.data.domain.Page;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.Customer;
import com.xorshop.common.exception.CustomerNotFoundException;
public interface ICustomerService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public void updateCustomerEnabledStatus(Integer id, boolean enabled);
	public List<Country> listAllCountries();
	public boolean isEmailUnique(Integer id, String email);
	public void save(Customer customerInForm);
	public void delete(Integer id) throws CustomerNotFoundException;
	public Customer get(Integer id) throws CustomerNotFoundException;
	List<Customer> listAll();
}