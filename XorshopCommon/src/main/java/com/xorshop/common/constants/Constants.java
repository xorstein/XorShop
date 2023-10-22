package com.xorshop.common.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Constants {

	private static final Logger LOGGER = LoggerFactory.getLogger(Constants.class);
	
	/*public static final String S3_BASE_URI;

	static {
		String bucketName = System.getenv("AWS_BUCKET_NAME");
		String region = System.getenv("AWS_REGION");
		String pattern = "https://%s.s3.%s.amazonaws.com";

		
		LOGGER.info("XorshopCommon | Constants | bucketName : " + bucketName);
		LOGGER.info("XorshopCommon | Constants | region : " + region);
		
		S3_BASE_URI = bucketName == null ? "" : String.format(pattern, bucketName, region);
		//S3_BASE_URI = "https://xorshop-files.s3.eu-north-1.amazonaws.com";

		
		LOGGER.info("XorShop | S3_BASE_URI | S3_BASE_URI : " + S3_BASE_URI);
	}*/
	


   /* public static final String DROPBOX_BASE_URI;

    static {
        String dropboxPath = "Apps/xorshop";
        String pattern = "https://www.dropbox.com/preview/%s";

        LOGGER.info("XorshopCommon | Constants | dropboxPath : " + dropboxPath);

        DROPBOX_BASE_URI = String.format(pattern, dropboxPath);

        LOGGER.info("XorShop | DROPBOX_BASE_URI | DROPBOX_BASE_URI : " + DROPBOX_BASE_URI);
    }*/
	
	public static final String S3_BASE_URI;

	 static {
	        String ftpServer = System.getenv("FTP_SERVER");
	        String ftpFolderRootName = System.getenv("FTP_FOLDER_ROOT_NAME");
	        String pattern = "https://%s/%s";

	        LOGGER.info("XorshopCommon | Constants | URL: " + ftpServer);
	        LOGGER.info("XorshopCommon | Constants | Folder: " + ftpFolderRootName);

	        if (ftpServer == null || ftpFolderRootName == null) {
	            S3_BASE_URI = ""; // or handle it in some way appropriate for your application
	        } else {
	            S3_BASE_URI = String.format(pattern, ftpServer, ftpFolderRootName);
	        }

	        // S3_BASE_URI = "https://xorshop-files.s3.eu-north-1.amazonaws.com"; // This line is commented out to avoid reassignment.
	        
	        LOGGER.info("XorShop | S3_BASE_URI | S3_BASE_URI: " + S3_BASE_URI);
	    }
	

	
}
