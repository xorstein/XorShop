package com.xorshop.admin.category;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.exporter.CategoryCsvExporter;
import com.xorshop.admin.exporter.CategoryExcelExporter;
import com.xorshop.admin.exporter.CategoryPdfExporter;
//import com.xorshop.admin.util.AmazonS3Util;
import com.xorshop.admin.util.CategoryPageInfo;
//import com.xorshop.admin.util.DropboxUtil;
import com.xorshop.admin.util.FTPUtil;
import com.xorshop.admin.util.FileUploadUtil;
import com.xorshop.common.exception.CategoryNotFoundException;
import com.xorshop.common.entity.Category ;

@Controller
public class CategoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listFirstPage(String sortDir, Model model) {
		
		LOGGER.info("CategoryController | listFirstPage is started");
		
		LOGGER.info("CategoryController | listFirstPage | sortDir : " + sortDir);
		
		return listByPage(1, sortDir, null, model);
	}
	
	@GetMapping("/categories/page/{pageNum}") 
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, 
			@RequestParam("sortDir") String sortDir, 
			@Param("keyword") String keyword,
			Model model) {
		
		LOGGER.info("CategoryController | listByPage is started");
		
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		
		List<com.xorshop.common.entity.Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);

		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("ListCategories", listCategories);
		model.addAttribute("activelink","5");
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("moduleURL", "/categories");

		
		LOGGER.info("CategoryController | listByPage | listCategories : " + listCategories.toString());
		LOGGER.info("CategoryController | listByPage | reverseSortDir : " + reverseSortDir);
		LOGGER.info("CategoryController | listByPage | totalPages : " + pageInfo.getTotalPages() );
		LOGGER.info("CategoryController | listByPage | totalItems : " + pageInfo.getTotalElements() );
		LOGGER.info("CategoryController | listByPage | currentPage : " + pageNum );
		LOGGER.info("CategoryController | listByPage | sortDir : " + sortDir);

		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		
		LOGGER.info("CategoryController | newCategory is started");
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        Category category= new Category();
        category.setEnabled(true);
		model.addAttribute("category", category);
		model.addAttribute("ListCategories", listCategories);
		model.addAttribute("PageTitle", "Nouvelle catégorie");
		model.addAttribute("activelink","5");
		LOGGER.info("CategoryController | newCategory | listCategories : " + listCategories.toString());

		return "categories/category_form";
		
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		
		LOGGER.info("CategoryController | saveCategory is started");
		
		LOGGER.info("CategoryController | saveCategory | multipartFile.isEmpty() : " + multipartFile.isEmpty());
		String message="";
        if(category.getId()==null) {
			message="la nouvelle catégorie "+category.getName()+" a été enregistré avec succès.";
		}
		else {
			message="La catégorie "+category.getName()+" été mise à jour.";
		}
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			LOGGER.info("CategoryController | saveCategory | fileName : " + fileName);
			
			category.setImage(fileName);

			Category savedCategory = categoryService.save(category);
			
			
			/*String uploadDir = "../category-images/" + savedCategory.getId();
			
			LOGGER.info("CategoryController | saveCategory | savedCategory : " + savedCategory.toString());
			LOGGER.info("CategoryController | saveCategory | uploadDir : " + uploadDir);
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);*/
			
			
			
			// Amazon S3 Image Storage
			String uploadDir = "category-images/" + savedCategory.getId();
			
			LOGGER.info("CategoryController | saveCategory | savedCategory : " + savedCategory.toString());
			LOGGER.info("CategoryController | saveCategory | uploadDir : " + uploadDir);
			
			//AmazonS3Util.removeFolder(uploadDir);
			//AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			//DropboxUtil.removeFolder(uploadDir);
			//DropboxUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			FTPUtil.removeFolder(uploadDir);
			FTPUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			
		} else {
			categoryService.save(category);
		}
				
		ra.addFlashAttribute("message", message);
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("CategoryController | editCategory is started");
		
		try {
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			LOGGER.info("CategoryController | editCategory | category : " + category.toString());
			LOGGER.info("CategoryController | editCategory | listCategories : " + listCategories.toString());
			
			model.addAttribute("activelink","5");
			model.addAttribute("category", category);
			model.addAttribute("ListCategories", listCategories);
			model.addAttribute("PageTitle", "Modification de la fiche catégorie n° " + id);

			return "categories/category_form";			
			
		} catch (CategoryNotFoundException ex) {
			
			LOGGER.info("CategoryController | editCategory | messageError : " + ex.getMessage());
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CategoryController | exportToCSV is started");
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		LOGGER.info("CategoryController | exportToCSV | listCategories : " + listCategories.toString());
		
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
		
		LOGGER.info("CategoryController | exportToCSV | export completed");
		
	}
	
	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CategoryController | exportToExcel is called");
		
		List<Category> listCategories = categoryService.listAll();
		
		LOGGER.info("CategoryController | exportToExcel | categoryService.listAll() : " + listCategories.size());

		CategoryExcelExporter exporter = new CategoryExcelExporter();
		
		LOGGER.info("CategoryController | exportToExcel | export is starting");
		
		exporter.export(listCategories, response);
		
		LOGGER.info("CategoryController | exportToExcel | export completed");
		
	}
	
	@GetMapping("/categories/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		
		LOGGER.info("CategoryController | exportToPDF is called");
		
		List<Category> listCategories = categoryService.listAll();
		
		LOGGER.info("CategoryController | exportToPDF | categoryService.listAll() : " + listCategories.size());
		
		CategoryPdfExporter exporter = new CategoryPdfExporter();
		
		LOGGER.info("CategoryController | exportToPDF | export is starting");
		
		exporter.export(listCategories, response);
		
		LOGGER.info("CategoryController | exportToPDF | export completed");
		
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		
		LOGGER.info("CategoryController | updateCategoryEnabledStatus is started");
		
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "activée" : "desactivée";
		String message = "La catégorie numéro " + id + " a été " + status;
		
		LOGGER.info("CategoryController | updateCategoryEnabledStatus | status : " + status);
		LOGGER.info("CategoryController | updateCategoryEnabledStatus | message : " + message);
		
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		LOGGER.info("CategoryController | deleteCategory is started");
		
		LOGGER.info("CategoryController | deleteCategory | id : " + id);
		
		try {
			categoryService.delete(id);
			
			LOGGER.info("CategoryController | deleteCategory | category deleted");
			
			
			//String categoryDir = "../category-images/" + id;
			
			/*LOGGER.info("CategoryController | deleteCategory | categoryDir : " + categoryDir);
			
			FileUploadUtil.removeDir(categoryDir);
			
			LOGGER.info("CategoryController | deleteCategory | FileUploadUtil.removeDir is over");*/
			
			
			// Amazon S3 Image Storage
			String categoryDir = "category-images/" + id;
			LOGGER.info("CategoryController | deleteCategory | categoryDir : " + categoryDir);
			
			//DropboxUtil.removeFolder(categoryDir);
			FTPUtil.removeFolder(categoryDir);
			LOGGER.info("CategoryController | deleteCategory | FileUploadUtil.removeDir is over");
			

			redirectAttributes.addFlashAttribute("message", 
					"La catégorie numéro" + id + " a été supprimé avec succès ");
			
			
		} catch (CategoryNotFoundException ex) {
			LOGGER.info("CategoryController | deleteCategory | messageError : " + ex.getMessage());
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/categories";
	}
}