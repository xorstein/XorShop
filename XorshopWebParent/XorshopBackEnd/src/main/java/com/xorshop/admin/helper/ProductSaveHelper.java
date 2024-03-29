package com.xorshop.admin.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xorshop.admin.util.FTPUtil;
//import com.xorshop.admin.util.AmazonS3Util;
//import com.xorshop.admin.util.FTPUtil;
import com.xorshop.admin.util.FileUploadUtil;
import com.xorshop.common.entity.product.Product;
import com.xorshop.common.entity.product.ProductImage;

public class ProductSaveHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

	public static void deleteExtraImagesWeredRemovedOnForm(Product product) {
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm is started");
		
		//String extraImageDir = "../product-images/" + product.getId() + "/extras";
		String extraImageDir = "product-images/" + product.getId() ;
		
		//List <String> listObjectKeys=AmazonS3Util.listFolder(extraImageDir+ "/extras");
		
		List <String> listObjectKeys=FTPUtil.listFolder(extraImageDir+ "/extras");
		
		//Path dirPath = Paths.get(extraImageDir);
		
		LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | listObjectKeys  : " + listObjectKeys.toString());
		 for(String objectKey :listObjectKeys) {
			 int lastIndexofShash=objectKey.lastIndexOf("/");
			 String fileName=objectKey.substring(lastIndexofShash+1,objectKey.length());
			 if (!product.containsImageName(fileName)) {
				 //AmazonS3Util.deleteFile(objectKey);
				 FTPUtil.deleteFile(objectKey);
				 LOGGER.info("Deleted extra image: " + objectKey);
			 }
		 }
		 LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm is done");

		/*try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + filename);

					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}

			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}*/
	}

	public static void  setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
			Product product) {
		
		LOGGER.info("ProductSaveHelper | setExistingExtraImageNames is started");
		if(imageIDs!=null && imageNames!=null ) {
			LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | imageIDs  : " + imageIDs.toString());
			LOGGER.info("ProductSaveHelper | deleteExtraImagesWeredRemovedOnForm | imageNames  : " + imageNames.toString());
				
		}
		
		if (imageIDs == null || imageIDs.length == 0) return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

	}
	
	public static void  setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		
		LOGGER.info("ProductSaveHelper | setNewExtraImageNames is started");
		
		LOGGER.info("ProductSaveHelper | setNewExtraImageNames | extraImageMultiparts.length : " + extraImageMultiparts.length);
		
		if (extraImageMultiparts.length > 0) {
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				LOGGER.info("ProductSaveHelper | setNewExtraImageNames | !multipartFile.isEmpty() : " + !multipartFile.isEmpty());
				
				if (!multipartFile.isEmpty()) {
					
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					
					LOGGER.info("ProductSaveHelper | setNewExtraImageNames | fileName : " + fileName);
					
					
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
					
					
				}
			}
		}
		
		LOGGER.info("ProductSaveHelper | setExtraImageNames is completed");
	}

	public static void  setMainImageName(MultipartFile mainImageMultipart, Product product) {
		
		LOGGER.info("ProductSaveHelper | setMainImageName is started");
		
		LOGGER.info("ProductSaveHelper | setMainImageName | !mainImageMultipart.isEmpty() : " + !mainImageMultipart.isEmpty());
		
		if (!mainImageMultipart.isEmpty()) {
			
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductSaveHelper | setMainImageName | fileName : " + fileName);
			
			product.setMainImage(fileName);
				
		}
		
		
		LOGGER.info("ProductSaveHelper | setMainImageName is completed");
	}
	
	public static void  saveUploadedImages(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages is started");
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages | !mainImageMultipart.isEmpty() : " + !mainImageMultipart.isEmpty());
		
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			LOGGER.info("ProductSaveHelper | saveUploadedImages | fileName : " + fileName);
			
			String uploadDir = "product-images/" + savedProduct.getId();
			//List <String> listObjectKeys=AmazonS3Util.listFolder(uploadDir+"/");
			List <String> listObjectKeys=FTPUtil.listFolder(uploadDir+"/");
			for(String objectKey: listObjectKeys) {
				if(!objectKey.contains("/extras/")) {
					FTPUtil.deleteFile(objectKey);
				}
			}
			
			LOGGER.info("ProductSaveHelper | saveUploadedImages | uploadDir : " + uploadDir);
			//String uploadDir = "../product-images/" + savedProduct.getId();
			//FileUploadUtil.cleanDir(uploadDir);
			
			//FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
			
			//AmazonS3Util.removeFolder(uploadDir);
			//AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());
			FTPUtil.removeFolder(uploadDir);
			FTPUtil.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());
		}
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages | extraImageMultiparts.length : " + extraImageMultiparts.length);
		
		if (extraImageMultiparts.length > 0) {
			
			//String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
			
			LOGGER.info("ProductSaveHelper | saveUploadedImages | uploadDir : " + uploadDir);

			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				LOGGER.info("ProductController | saveUploadedImages | multipartFile.isEmpty() : " + multipartFile.isEmpty());
				if (multipartFile.isEmpty()) continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
				LOGGER.info("ProductSaveHelper | saveUploadedImages | fileName : " + fileName);
				//AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
				FTPUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
				//FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				
			}
		}
		
		
		LOGGER.info("ProductSaveHelper | saveUploadedImages is completed");
	}
	
	public static void  setProductDetails(String[] detailIDs, String[] detailNames, 
			String[] detailValues, Product product) {
		
		LOGGER.info("ProductSaveHelper | setProductDetails is started");
		
		LOGGER.info("ProductSaveHelper | setProductDetails | detailNames : " + detailNames.toString());
		LOGGER.info("ProductSaveHelper | setProductDetails | detailNames : " + detailValues.toString());
		LOGGER.info("ProductSaveHelper | setProductDetails | product : " + product.toString());
		
		
		if (detailNames == null || detailNames.length == 0) return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
		
		LOGGER.info("ProductSaveHelper | setProductDetails | product with its detail : " + product.getDetails().toString());
		
		LOGGER.info("ProductSaveHelper | setProductDetails is completed");
	}
	
	
	
	
	
}


