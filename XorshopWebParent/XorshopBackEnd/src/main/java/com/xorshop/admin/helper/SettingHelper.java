package com.xorshop.admin.helper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xorshop.admin.currency.CurrencyRepository;
import com.xorshop.admin.setting.SettingService;
//import com.xorshop.admin.util.DropboxUtil;
import com.xorshop.admin.util.FTPUtil;
//import com.xorshop.admin.util.AmazonS3Util;
import com.xorshop.admin.util.FileUploadUtil;
import com.xorshop.admin.util.GeneralSettingBag;
import com.xorshop.common.entity.setting.Setting;

public class SettingHelper {

	public static void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag,String name) throws IOException {
		if (!multipartFile.isEmpty()) {
			 String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			 String value = "/site-logo/" + fileName;
			 String path=settingBag.get(name).getValue();	
			 String OldFile = Paths.get(path).getFileName().toString();
			 settingBag.updateSiteLogo(name,value);
			 			 
				
			
			            
			/* Image Folder*/ 
			/*String uploadDir = "../site-logo/";
			FileUploadUtil.DeleteFileDirectory(uploadDir,path);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);*/
			
			
			// Amazon S3 Image Storage
			String uploadDir = "site-logo";
			
			//AmazonS3Util.deleteFile(uploadDir+"/"+ OldFile);
			//AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			// Dropbox Image Storage
			
			//DropboxUtil.deleteFile(uploadDir+"/"+ OldFile);
			//DropboxUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
			// FTP Image Storage
			
			FTPUtil.deleteFile(uploadDir+"/"+ OldFile);
			FTPUtil.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		}
	}

	public static void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag,CurrencyRepository currencyRepo) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<com.xorshop.common.entity.Currency> findByIdResult = currencyRepo.findById(currencyId);

		if (findByIdResult.isPresent()) {
			com.xorshop.common.entity.Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	public static void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings,SettingService service) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}

		service.saveAll(listSettings);
	}
}
