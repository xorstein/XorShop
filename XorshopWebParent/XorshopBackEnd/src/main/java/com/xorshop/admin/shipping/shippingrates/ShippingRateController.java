package com.xorshop.admin.shipping.shippingrates;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.ShippingRate;
import com.xorshop.common.exception.ShippingRateAlreadyExistsException;
import com.xorshop.common.exception.ShippingRateNotFoundException;

@Controller
public class ShippingRateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingRateController.class);

	private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";

	@Autowired 
	private ShippingRateService service;
	
	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		
		LOGGER.info("ShippingRateController | listFirstPage is called");
		
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "shippingRates", 
						moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
						@PathVariable(name = "pageNum") int pageNum,Model model) {
		
		LOGGER.info("ShippingRateController | listByPage is called");
		
		LOGGER.info("ShippingRateController | listByPage | helper : " + helper.toString());
		LOGGER.info("ShippingRateController | listByPage | pageNum : " + pageNum);
		
		service.listByPage(pageNum, helper);
		model.addAttribute("activelink","9");
		return "shipping_rates/shipping_rates";
	}	

	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		
		
		LOGGER.info("ShippingRateController | newRate is called");
			
		List<Country> listCountries = service.listAllCountries();
		
		LOGGER.info("ShippingRateController | newRate | listCountries : " + listCountries.toString());
		ShippingRate rate= new ShippingRate();
		rate.setCodSupported(true);
		model.addAttribute("rate", rate);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Configurer un nouveau frais d'expédition");
		model.addAttribute("activelink","9");

		return "shipping_rates/shipping_rate_form";		
	}

	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate rate, RedirectAttributes ra) {
		
		LOGGER.info("ShippingRateController | saveRate is called");
		
		try {
			service.save(rate);
			
			LOGGER.info("ShippingRateController | saveRate | message : " + "The shipping rate has been saved successfully.");
			
			ra.addFlashAttribute("message", "Le frais d'expédition a été enregistré avec succès.");
		} catch (ShippingRateAlreadyExistsException ex) {
			
			LOGGER.info("ShippingRateController | saveRate | message : " + ex.getMessage());
			
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		
		LOGGER.info("ShippingRateController | editRate is called");
		
		
		try {
			ShippingRate rate = service.get(id);
			
			LOGGER.info("ShippingRateController | editRate | rate : " + rate.toString());
			
			List<Country> listCountries = service.listAllCountries();
			
			LOGGER.info("ShippingRateController | editRate | listCountries : " + listCountries.toString());

			model.addAttribute("listCountries", listCountries);			
			model.addAttribute("rate", rate);
			model.addAttribute("pageTitle", "Modifier le frais d'éxpedition (ID: " + id + ")");
			model.addAttribute("activelink","9");

			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException ex) {
			
			LOGGER.info("ShippingRateController | saveRate | message : " + ex.getMessage());
			
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "supported") Boolean supported,
			Model model, RedirectAttributes ra) {
		
		LOGGER.info("ShippingRateController | updateCODSupport is called");
		
		try {
			service.updateCODSupport(id, supported);
			
			LOGGER.info("ShippingRateController | updateCODSupport | message : " + "COD support for shipping rate ID " + id + " has been updated.");
			
			ra.addFlashAttribute("message", "COD de support pour le frais d'éxpedition numéro " + id + " a été mis à jour.");
		} catch (ShippingRateNotFoundException ex) {
			
			LOGGER.info("ShippingRateController | updateCODSupport | message : " + ex.getMessage());
			
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		
		LOGGER.info("ShippingRateController | deleteRate is called");
		
		try {
			service.delete(id);
			
			LOGGER.info("ShippingRateController | deleteRate | message : " + "The shipping rate ID " + id + " has been deleted.");
			
			ra.addFlashAttribute("message", "Le frais d'expédition numéro" + id + " a été supprimé.");
		} catch (ShippingRateNotFoundException ex) {
			
			LOGGER.info("ShippingRateController | deleteRate | message : " + ex.getMessage());
			
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}	
}

