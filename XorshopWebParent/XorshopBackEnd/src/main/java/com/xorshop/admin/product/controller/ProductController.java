package com.xorshop.admin.product.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.xorshop.admin.helper.ProductSaveHelper;
import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
import com.xorshop.admin.product.ProductNotFoundException;
import com.xorshop.admin.product.ProductService;
import com.xorshop.admin.security.XorshopUserDetails;
//import com.xorshop.admin.util.AmazonS3Util;
import com.xorshop.admin.util.CategoryPageInfo;
//import com.xorshop.admin.util.DropboxUtil;
import com.xorshop.admin.util.FTPUtil;
import com.xorshop.admin.util.FileUploadUtil;
import com.xorshop.admin.brand.BrandService;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;
import com.xorshop.common.entity.product.Product;
import com.xorshop.common.entity.product.ProductImage;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private BrandService brandService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	private String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
	private String defaultRedirectURLAffected = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0&keyword=";

	@GetMapping("/products")
	public String listFirstPage() {
		LOGGER.info("ProductController | listFirstPage is started");
		return defaultRedirectURL;
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model, @RequestParam("categoryId") Integer categoryId) {

		LOGGER.info("ProductController | listByPage is started");
		productService.listByPage(pageNum, helper, categoryId);
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		if (categoryId != null)
			model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("activelink", "4");
		LOGGER.info("ProductController | newProduct | categoryId : " + categoryId);
		LOGGER.info("ProductController | newProduct | listCategories : " + listCategories.toString());

		return "products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {

		LOGGER.info("ProductController | newProduct is started");

		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		LOGGER.info("ProductController | newProduct | product : " + product);
		LOGGER.info("ProductController | newProduct | listBrands : " + listBrands.size());

		model.addAttribute("product", product);
		model.addAttribute("listbrands", listBrands);
		model.addAttribute("activelink", "4");
		model.addAttribute("PageTitle", "Nouveau produit");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal XorshopUserDetails loggedUser) throws IOException {
		String message = "";
		Integer id = 0;
		if (product.getId() == null) {
			message = "Le nouveau produit " + product.getName() + " a été enregistré avec succès.";
		} else {
			message = "Le produit " + product.getName() + " été mise à jour.";
			id = product.getId();
		}
		LOGGER.info("ProductController | saveProduct is started");
		if (mainImageMultipart != null)
			LOGGER.info(
					"ProductController | saveProduct | mainImageMultipart.isEmpty() : " + mainImageMultipart.isEmpty());
		if (extraImageMultiparts != null)
			LOGGER.info("ProductController | saveProduct | extraImageMultiparts size : " + extraImageMultiparts.length);

		LOGGER.info("ProductController | saveProduct | loggedUser.getUsername() : " + loggedUser.getUsername());
		LOGGER.info("ProductController | saveProduct | loggedUser.getFullname() : " + loggedUser.getFullname());
		LOGGER.info("ProductController | saveProduct | loggedUser.getAuthorities() : " + loggedUser.getAuthorities());
		LOGGER.info("ProductController | saveProduct | loggedUser.getAuthorities() : " + product.toString());

		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editeur")) {
			if (loggedUser.hasRole("Vendeur")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message", "Le produit avec id " + id + " été mise à jour.");
				return defaultRedirectURL;
			}
		}

		ProductSaveHelper.setMainImageName(mainImageMultipart, product);

		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);

		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);

		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

		Product savedProduct = productService.save(product);

		/*
		 * Image Folder ProductSaveHelper.saveUploadedImages(mainImageMultipart,
		 * extraImageMultiparts, savedProduct);
		 * ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		 */

		// Amazon S3 Image Storage
		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);

		ra.addFlashAttribute("activelink", "4");
		ra.addFlashAttribute("message", message);

		return defaultRedirectURLAffected + savedProduct.getName();

	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) throws ProductNotFoundException {

		LOGGER.info("ProductController | updateCategoryEnabledStatus is started");
		Product product = productService.get(id);
		productService.updateProductEnabledStatus(id, enabled);
		String Status = enabled ? "Actif" : "Inactif";

		LOGGER.info("ProductController | updateCategoryEnabledStatus | status : " + Status);

		String message = "Produit numéro " + id + " est " + Status;
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("activelink", "4");
		return defaultRedirectURLAffected + product.getName();
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		LOGGER.info("ProductController | deleteProduct is started");

		try {
			Product p = productService.get(id);
			productService.delete(id);

			// String productExtraImagesDir = "../product-images/" + id + "/extras";
			// String productImagesDir = "../product-images/" + id;

			String productExtraImagesDir = "product-images/" + id + "/extras";
			String productImagesDir = "product-images/" + id;

			LOGGER.info("ProductController | deleteProduct | productExtraImagesDir : " + productExtraImagesDir);
			LOGGER.info("ProductController | deleteProduct | productImagesDir : " + productImagesDir);

			// Image Folder
			// FileUploadUtil.removeDir(productExtraImagesDir);
			// FileUploadUtil.removeDir(productImagesDir);

			// Amazon S3 option
			// AmazonS3Util.removeFolder(productExtraImagesDir);
			// AmazonS3Util.removeFolder(productImagesDir);

			// Dropbox option
			//DropboxUtil.removeFolder(productExtraImagesDir);
			//DropboxUtil.removeFolder(productImagesDir);

			// FTP option
			FTPUtil.removeFolder(productExtraImagesDir);
			FTPUtil.removeFolder(productImagesDir);

			LOGGER.info("ProductController | deleteProduct is done");

			model.addAttribute("activelink", "4");
			redirectAttributes.addFlashAttribute("message",
					"Le produit " + p.getName() + " numéro P." + id + " a été  supprimé avec succès");
		} catch (ProductNotFoundException ex) {

			LOGGER.info("ProductController | deleteProduct | messageError : " + ex.getMessage());

			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return defaultRedirectURL;
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			@AuthenticationPrincipal XorshopUserDetails loggedUser) {

		LOGGER.info("ProductController | editProduct is started");

		try {
			Product product = productService.get(id);
			List<Brand> listbrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			LOGGER.info("ProductController | editProduct | loggedUser  : " + loggedUser.toString());

			boolean isReadOnlyForSalesperson = false;

			if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editeur")) {
				if (loggedUser.hasRole("Vendeur")) {
					isReadOnlyForSalesperson = true;
				}
			}

			LOGGER.info("ProductController | editProduct | product  : " + product.toString());
			LOGGER.info("ProductController | editProduct | listBrands : " + listbrands.toString());
			LOGGER.info(
					"ProductController | editProduct | numberOfExistingExtraImages : " + numberOfExistingExtraImages);
			LOGGER.info("ProductController | editProduct | isReadOnlyForSalesperson  : " + isReadOnlyForSalesperson);

			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("activelink", "4");
			model.addAttribute("listbrands", listbrands);
			model.addAttribute("PageTitle", "Modification de la fiche produit n° " + id);
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";

		} catch (ProductNotFoundException e) {

			LOGGER.info("ProductController | editProduct | error : " + e.getMessage());

			ra.addFlashAttribute("messageError", e.getMessage());

			return defaultRedirectURL;
		}

	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

		LOGGER.info("ProductController | viewProductDetails is started");

		try {
			Product product = productService.get(id);

			LOGGER.info("ProductController | viewProductDetails  | product : " + product.toString());

			model.addAttribute("product", product);

			return "products/product_detail_modal";

		} catch (ProductNotFoundException e) {

			LOGGER.info("ProductController | viewProductDetails  | messageError : " + e.getMessage());

			ra.addFlashAttribute("messageError", e.getMessage());

			return defaultRedirectURL;
		}
	}

}
