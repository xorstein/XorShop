package com.xorshop.admin.shipping.shippingrates;

import java.util.List;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.ShippingRate;
import com.xorshop.common.exception.ShippingRateAlreadyExistsException;
import com.xorshop.common.exception.ShippingRateNotFoundException;

public interface IShippingRateService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public List<Country> listAllCountries();
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException;
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException;
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException;
	public void delete(Integer id) throws ShippingRateNotFoundException;
	
}
