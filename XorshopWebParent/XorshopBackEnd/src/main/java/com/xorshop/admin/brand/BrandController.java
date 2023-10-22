package com.xorshop.admin.brand;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.category.CategoryService;
import com.xorshop.admin.exporter.BrandCsvExporter;
import com.xorshop.admin.exporter.BrandExcelExporter;
import com.xorshop.admin.exporter.BrandPdfExporter;
import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
//import com.xorshop.admin.util.AmazonS3Util;
//import com.xorshop.admin.util.DropboxUtil;
import com.xorshop.admin.util.FTPUtil;
import com.xorshop.admin.util.FileUploadUtil;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;
import com.xorshop.common.exception.BrandNotFoundException;

@Controller
public class BrandController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

	private BrandService brandService;
	
	private CategoryService categoryService;
	
	private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";
	private String defaultRedirectURLExactely = "redirect:/brands/page/1?sortField=name&sortDir=asc&keyword=";
	
	@Autowired
	public BrandController(BrandService brandService, CategoryService categoryService) {
		super();
		this.brandService = brandService;
		this.categoryService = categoryService;
	}


	@GetMapping("/brands")
	public String listFirstPage() {
		
		LOGGER.info("BrandController | listByPage is started");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		LOGGER.info("BrandController | newBrand is started");
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("activelink","6");
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("PageTitle", "Nouvelle marque");
		
		LOGGER.info("CategoryController | newBrand | listCategories : " + listCategories.toString());

		return "brands/brand_form";		
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		
		LOGGER.info("BrandController | saveBrand is started");
		
		LOGGER.info("BrandController | saveBrand | multipartFile.isEmpty() : " + multipartFile.isEmpty());
		String message="";
		if(brand.getId()==null) {
			message="La nouvelle marque "+brand.getName()+" a été enregistré avec succès.";
		}
		else {
			message="La marque "+brand.getName()+" été mise à jour.";
		}
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			LOGGER.info("BrandController | saveCategory | fileName : " + fileName);
			
			brand.setLogo(fileName);

			
			Brand savedBrand = brandService.save(brand);
			
			
			/*String uploadDir = "../brand-images/" + savedBrand.getId();
			
			LOGGER.info("BrandController | saveBrand | savedBrand : " + savedBrand.toString());
			LOGGER.info("BrandController | saveBrand | uploadDir : " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);*/
			
			
			// Amazon S3 Image Storage
			String uploadDir = "brand-images/" + savedBrand.getId();
			
			LOGGER.info("BrandController | saveBrand | savedBrand : " + savedBrand.toString());
			LOGGER.info("BrandController | saveBrand | uploadDir : " + uploadDir);

			//AmazonS3Util.removeFolder(uploadDir);
			//AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			//DropboxUtil.removeFolder(uploadDir);
			//DropboxUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			FTPUtil.removeFolder(uploadDir);
			FTPUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			

		} else {
			brandService.save(brand);
		}
		
		ra.addFlashAttribute("message",message);
		return defaultRedirectURLExactely+brand.getName();		
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		
		LOGGER.info("BrandController | editBrand is started");
		
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			LOGGER.info("BrandController | editBrand | brand : " + brand.toString());
			LOGGER.info("BrandController | editBrand | listCategories : " + listCategories.toString());
			model.addAttribute("activelink","6");
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("PageTitle", "Modification de la fiche catégorie n° " + id);

			return "brands/brand_form";			
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		LOGGER.info("BrandController | deleteBrand is started");
		
		LOGGER.info("BrandController | deleteBrand | id : " + id);
		
		try {
			brandService.delete(id);
			
			LOGGER.info("BrandController | deleteBrand | brand deleted");
			
			 
			/*String brandDir = "../brand-images/"+id;
			
			LOGGER.info("BrandController | deleteBrand | brandDir : " + brandDir);
			
			FileUploadUtil.removeDir(brandDir);
			
			LOGGER.info("BrandController | deleteBrand | FileUploadUtil.removeDir is over");*/
			
			
			// Amazon S3 Image Storage
			String brandDir = "brand-images/" + id;
			LOGGER.info("BrandController | deleteBrand | brandDir : " + brandDir);
			
			//AmazonS3Util.removeFolder(brandDir);
			//DropboxUtil.removeFolder(brandDir);
			FTPUtil.removeFolder(brandDir);
			LOGGER.info("BrandController | deleteBrand | FileUploadUtil.removeDir is over");

			redirectAttributes.addFlashAttribute("message", "La marque numéro " + id + " a été  supprimé avec succès");;
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;
	}	
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,Model model
			) {
		
		LOGGER.info("BrandController | listByPage is started");
		
		brandService.listByPage(pageNum, helper);
		model.addAttribute("activelink","6");
		return "brands/brands";		
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		LOGGER.info("BrandController | exportToCSV is started");
		
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandController | exportToCSV | listBrands : " + listBrands.toString());
		
		BrandCsvExporter exporter = new BrandCsvExporter();
		exporter.export(listBrands, response);
		
		LOGGER.info("BrandController | exportToCSV | export completed");
		
	}
	
	@GetMapping("/brands/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		LOGGER.info("BrandController | exportToExcel is called");
		
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandController | exportToExcel | listBrands Size : " + listBrands.size());

		BrandExcelExporter exporter = new BrandExcelExporter();
		
		LOGGER.info("BrandController | exportToExcel | export is starting");
		
		exporter.export(listBrands, response);
		
		LOGGER.info("BrandController | exportToExcel | export completed");
		
	}
	
	@GetMapping("/brands/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		
		LOGGER.info("BrandController | exportToPDF is called");
		
		List<Brand> listBrands = brandService.listAll();
		
		LOGGER.info("BrandController | exportToPDF | listBrands Size : " + listBrands.size());
		
		BrandPdfExporter exporter = new BrandPdfExporter();
		
		LOGGER.info("BrandController | exportToPDF | export is starting");
		
		exporter.export(listBrands, response);
		
		LOGGER.info("BrandController | exportToPDF | export completed");
		
	}
	
}
