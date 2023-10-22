package com.xorshop.admin.shipping.shippingrates;

import java.util.List;
import java.util.NoSuchElementException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xorshop.admin.country.CountryRepository;
import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.product.ProductRepository;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.ShippingRate;
import com.xorshop.common.entity.product.Product;
import com.xorshop.common.exception.ShippingRateAlreadyExistsException;
import com.xorshop.common.exception.ShippingRateNotFoundException;

@Service
@Transactional
public class ShippingRateService implements IShippingRateService{
	
	public static final int RATES_PER_PAGE = 10;
	private static final int DIM_DIVISOR = 139;	
 
	private ShippingRateRepository shipRepo;
	
	private CountryRepository countryRepo;
	
	private ProductRepository productRepo;
	
	@Autowired
	public ShippingRateService(ShippingRateRepository shipRepo, CountryRepository countryRepo,ProductRepository productRepo) {
		super();
		this.shipRepo = shipRepo;
		this.countryRepo = countryRepo;
		this.productRepo = productRepo;
	}

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}
	
	@Override
	public List<Country> listAllCountries() {
		// TODO Auto-generated method stub
		return countryRepo.findAllByOrderByNameAsc();
	}

	@Override
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		// TODO Auto-generated method stub
		ShippingRate rateInDB = shipRepo.findByCountryAndState(
				rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("Il y a déjà un tarif pour la destination"
						+ rateInForm.getCountry().getName() + ", " + rateInForm.getState()); 					
		}
		shipRepo.save(rateInForm);
	}

	@Override
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		// TODO Auto-generated method stub
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Impossible de trouver le tarif d'expédition avec le numéro " + id);
		}
	}

	@Override
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		// TODO Auto-generated method stub
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Impossible de trouver le tarif d'expédition avec le numéro " + id);
		}

		shipRepo.updateCODSupport(id, codSupported);
	}

	@Override
	public void delete(Integer id) throws ShippingRateNotFoundException {
		// TODO Auto-generated method stub
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Impossible de trouver le tarif d'expédition avec le numéro " + id);

		}
		shipRepo.deleteById(id);
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId, String state) 
			throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);

		if (shippingRate == null) {
			throw new ShippingRateNotFoundException("Aucun tarif d'expédition trouvé pour la "
					+ "destination. Vous devez saisir les frais d'expédition manuellement.");
		}

		Product product = productRepo.findById(productId).get();

		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

		return finalWeight * shippingRate.getRate();
	}

}

