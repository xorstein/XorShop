package com.xorshop.admin.role;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
import com.xorshop.common.entity.Role;


@Controller
public class RoleController {
	
	@Autowired
	private RoleService service;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
	
	
	private String defaultRedirectURL = "redirect:/roles/page/1?sortField=name&sortDir=asc";
	
	@GetMapping("/roles")
	public String listFirstPage() {
		LOGGER.info("RoleController | listFirstPage is started");
		return defaultRedirectURL;
	}
	@GetMapping("/roles/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listRoles", moduleURL = "/roles") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model
			) {
		List<Role> listRoles = service.findAll();
		model.addAttribute("listRoles", listRoles);
		LOGGER.info("RoleController | listByPage is started");
		service.listByPage(pageNum, helper);
		
;
		model.addAttribute("activelink","1");
		LOGGER.info("RoleController | listFirstPage is ended");

		return "roles/roles";
	}	

}
