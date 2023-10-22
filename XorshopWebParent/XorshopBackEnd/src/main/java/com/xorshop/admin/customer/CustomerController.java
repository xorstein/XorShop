package com.xorshop.admin.customer;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.exporter.CustomerCsvExporter;
import com.xorshop.admin.exporter.CustomerExcelExporter;
import com.xorshop.admin.exporter.CustomerPdfExporter;
import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.Customer;
import com.xorshop.common.exception.CustomerNotFoundException;
@Controller
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired 
	private CustomerService service;
	
	private String defaultRedirectURL = "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	
	@GetMapping("/customers")
	public String listFirstPage() {
		
		LOGGER.info("CustomerController | listFirstPage is called");
		
		return defaultRedirectURL;
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,Model model) {

		service.listByPage(pageNum, helper);
		model.addAttribute("activelink","11");
		return "customers/customers";
	}

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		
		LOGGER.info("CustomerController | updateCustomerEnabledStatus is called");
		
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "activé" : "désactivé";
		String message = "Le client numéro " + id + " à été " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		LOGGER.info("CustomerController | listByPage | status : " + status);
		LOGGER.info("CustomerController | listByPage | message : " + message);
		

		return defaultRedirectURL;
	}	

	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | viewCustomer is called");
		
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);
			model.addAttribute("activelink","11");
			LOGGER.info("CustomerController | viewCustomer | customer : " + customer.toString());
			
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("CustomerController | viewCustomer | message : " + ex.getMessage());
			
			return defaultRedirectURL;			
		}
	}

	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | editCustomer is called");
		
		try {
			Customer customer = service.get(id);
			List<Country> countries = service.listAllCountries();

			model.addAttribute("listCountries", countries);			
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Modification de la fiche client (numéro: %d)", id));
			model.addAttribute("activelink","11");
			LOGGER.info("CustomerController | editCustomer | listCountries : " + countries);
			LOGGER.info("CustomerController | editCustomer | customer : " + customer);
			LOGGER.info("CustomerController | editCustomer | pageTitle : " + (String.format("Edit Customer (ID: %d)", id)));

			return "customers/customer_form";

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("CustomerController | editCustomer | message : " + ex.getMessage());
			
			return defaultRedirectURL;
		}
	}

	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | saveCustomer is called");
		String message="";
		if(customer.getId()!=null) {
			message=" a été ajouté.";
		}
		else {
			message=" a été mise à jour.";
		}
		service.save(customer);
		ra.addFlashAttribute("message", "Le client " + customer.getFullName() + message);
		
		LOGGER.info("CustomerController | editCustomer | message : " + "The customer ID " + customer.getId() + " has been updated successfully.");
		
		return defaultRedirectURL;
	}

	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
		
		LOGGER.info("CustomerController | deleteCustomer is called");
		
		try {
			service.delete(id);			
			ra.addFlashAttribute("message", "Le client numéro" + id + " a été supprimé.");
			
			LOGGER.info("CustomerController | deleteCustomer | message : " + "The customer ID " + id + " has been deleted successfully.");

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			LOGGER.info("CustomerController | deleteCustomer | message : " + ex.getMessage());
		}

		return defaultRedirectURL;
	}
	
	@GetMapping("/customers/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CustomerController | exportToCSV is called");
		
		List<Customer> listCustomers = service.listAll();
		
		LOGGER.info("CustomerController | exportToCSV | listCustomers.size() : " + listCustomers.size());
		
		CustomerCsvExporter exporter = new CustomerCsvExporter();
		
		LOGGER.info("CustomerController | exportToCSV | export is starting");
		
		exporter.export(listCustomers, response);
		
		LOGGER.info("CustomerController | exportToCSV | export completed");
	}
	
	@GetMapping("/customers/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CustomerController | exportToExcel is called");
		
		List<Customer> listCustomers = service.listAll();
		
		LOGGER.info("CustomerController | exportToExcel | listCustomers.size() : " + listCustomers.size());

		CustomerExcelExporter exporter = new CustomerExcelExporter();
		
		LOGGER.info("CustomerController | exportToExcel | export is starting");
		
		exporter.export(listCustomers, response);
		
		LOGGER.info("CustomerController | exportToExcel | export completed");
	}
	
	@GetMapping("/customers/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CustomerController | exportToPDF is called");
		
		List<Customer> listCustomers = service.listAll();
		
		LOGGER.info("UserController | exportToPDF | listCustomers.size() : " + listCustomers.size());
		
		CustomerPdfExporter exporter = new CustomerPdfExporter();
		
		LOGGER.info("UserController | exportToPDF | export is starting");
		
		exporter.export(listCustomers, response);
		
		LOGGER.info("CustomerController | exportToPDF | export completed");
	}	
}

