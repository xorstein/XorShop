package com.xorshop.admin.section;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.xorshop.admin.util.SectionUtil;
import com.xorshop.common.entity.section.Section;
import com.xorshop.common.entity.section.SectionType;
import com.xorshop.common.exception.SectionNotFoundException;

@Controller
public class ProductSectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSectionController.class);
	
	@Autowired
	private SectionService service;

	@GetMapping("/sections/new/product")
	public String showForm(Model model) {
		
		LOGGER.info("ProductSectionController | showForm is called");
		
		Section section = new Section(true, SectionType.PRODUCT);

		LOGGER.info("GeneralSectionController | listAllSections | section : " + section.toString());
		LOGGER.info("GeneralSectionController | listAllSections | pageTitle : " + "Add Product Section");
		model.addAttribute("activelink", "16");
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Section de produits - Nouvel element");

		return "sections/product_section_form";
	}


	@PostMapping("/sections/save/product")
	public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
		SectionUtil.addProductsToSection(section, request);
		service.saveSection(section);
		LOGGER.info("ProductSectionController | saveSection | message : " + "The section of type Product has been saved successfully.");
		ra.addFlashAttribute("message", "La section de type produit a été enregistrée avec succès.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/Product/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("ProductSectionController | editSection is called");
		
		try {
			Section section = service.getSection(id);
			
			LOGGER.info("ProductSectionController | editSection | section : " + section.toString());
			LOGGER.info("ProductSectionController | editSection | pageTitle : " + "Edit Product Section (ID: " + id + ")");
			model.addAttribute("activelink", "16");
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Section de produits - Modifier l'element (Numéro: " + id + ")");

			return "sections/product_section_form";

		} catch (SectionNotFoundException ex) {
			LOGGER.info("ProductSectionController | editSection | error : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
			return "redirect:/sections";		
		}

	}
}
