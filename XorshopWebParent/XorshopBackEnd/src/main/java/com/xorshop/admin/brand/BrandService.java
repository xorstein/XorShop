package com.xorshop.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.exception.BrandNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService implements IBrandService{
	
	public static final int BRANDS_PER_PAGE = 10;

	@Autowired
	private BrandRepository repo;
	
	@Override
	public List<Brand> listAll() {
		// TODO Auto-generated method stub
		
		Sort firstNameSorting =  Sort.by("name").ascending();
		
		List<Brand> brandList = new ArrayList<>();
		repo.findAll(firstNameSorting).forEach(brandList::add);
		return brandList;
	
	}

	@Override
	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	@Override
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	@Override
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);			
		}

		repo.deleteById(id);
	}

	@Override
	public String checkUnique(Integer id, String name) {
		// TODO Auto-generated method stub
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repo.findByName(name);

		if (isCreatingNew) {
			if (brandByName != null) return "Nom Dupliqué";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Nom Dupliqué";
			}
		}

		return "OK";
	}

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
	}
}