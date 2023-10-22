package com.xorshop.admin.section;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.xorshop.common.entity.section.Section;
import com.xorshop.common.entity.section.SectionType;
import com.xorshop.common.exception.SectionNotFoundException;

@Controller
public class TextSectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TextSectionController.class);
	
	@Autowired
	private SectionService service;
	
	@GetMapping("/sections/new/text")
	public String showForm(Model model) {
		
		LOGGER.info("TextSectionController | showForm is called");
		
		Section section = new Section(true, SectionType.TEXT);

		LOGGER.info("TextSectionController | showForm | section : " + section.toString());
		LOGGER.info("TextSectionController | showForm | pageTitle : " + "Add Text Section");
		model.addAttribute("activelink", "16");
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Section de textes - Nouvel element");

		return "sections/text_section_form";
	}	

	@PostMapping("/sections/save/text")
	public String saveSection(Section section, RedirectAttributes ra) {
		
		LOGGER.info("TextSectionController | saveSection is called");
		
		service.saveSection(section);
		
		LOGGER.info("TextSectionController | saveSection | messageSuccess : " + "The section of type Text has been saved successfully.");
		ra.addFlashAttribute("message", "La section de type texte a été enregistrée avec succès.");
		return "redirect:/sections";
	}		

	@GetMapping("/sections/edit/Text/{id}")
	public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("TextSectionController | editSection is called");
		
		try {
			Section section = service.getSection(id);

			LOGGER.info("TextSectionController | editSection | section : " + section.toString());
			LOGGER.info("TextSectionController | editSection | pageTitle : " + "Edit Text Section (ID: " + id + ")");
			model.addAttribute("activelink", "16");
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Section de textes - Modifier l'element (Numéro:: " + id + ")");

			return "sections/text_section_form";

		} catch (SectionNotFoundException ex) {
			LOGGER.info("TextSectionController | editSection | messageError : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
			return "redirect:/sections";		
		}

	}
}
