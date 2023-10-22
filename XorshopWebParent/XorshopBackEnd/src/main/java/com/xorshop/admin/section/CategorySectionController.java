package com.xorshop.admin.section;

import java.util.List;

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

import com.xorshop.admin.category.CategoryService;
import com.xorshop.admin.util.SectionUtil;
import com.xorshop.common.entity.Category;
import com.xorshop.common.entity.section.Section;
import com.xorshop.common.entity.section.SectionType;
import com.xorshop.common.exception.SectionNotFoundException;

@Controller
public class CategorySectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategorySectionController.class);
	
	private SectionService sectionService;

	private CategoryService categoryService;
	
	@Autowired
	public CategorySectionController(SectionService sectionService, CategoryService categoryService) {
		super();
		this.sectionService = sectionService;
		this.categoryService = categoryService;
	}

	@GetMapping("/sections/new/category")
	public String showForm(Model model) {
		
		LOGGER.info("CategorySectionController | showForm is called");
		
		Section section = new Section(true, SectionType.CATEGORY);

		List<Category> listCategories = categoryService.listAll();

		LOGGER.info("CategorySectionController | showForm | listCategories size : " + listCategories.size());
		LOGGER.info("CategorySectionController | showForm | section : " + section.toString());
		LOGGER.info("CategorySectionController | showForm | pageTitle : " + "Add Category Section");
		model.addAttribute("activelink", "16");
		model.addAttribute("listCategories", listCategories);		
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Section de catégories - Nouvel element");

		return "sections/category_section_form";
	}	

	@PostMapping("/sections/save/category")
	public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("CategorySectionController | saveSection is called");
		
		SectionUtil.addCategoriesToSection(section, request);
		sectionService.saveSection(section);
		
		LOGGER.info("CategorySectionController | saveSection | message : " + "The section of type Category has been saved successfully.");
		
		ra.addFlashAttribute("message", "La section de type catégorie a été enregistrée avec succès..");

		return "redirect:/sections";
	}	

	@GetMapping("/sections/edit/Category/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("CategorySectionController | editSection is called");
		
		try {
			Section section = sectionService.getSection(id);
			List<Category> listCategories = categoryService.listAll();
			
			LOGGER.info("CategorySectionController | editSection | listCategories size : " + listCategories.size());
			LOGGER.info("CategorySectionController | editSection | section : " + section.toString());
			LOGGER.info("CategorySectionController | editSection | pageTitle : " + "Edit Category Section (ID: " + id + ")");
			model.addAttribute("activelink", "16");
			model.addAttribute("listCategories", listCategories);			
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Section de catégories - Modifier l'element (Numéro: " + id + ")");

			return "sections/category_section_form";

		} catch (SectionNotFoundException ex) {
			LOGGER.info("CategorySectionController | editSection | error : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
			return "redirect:/sections";		
		}

	}	
}
