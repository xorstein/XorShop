package com.xorshop.admin.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.xorshop.admin.util.FTPUtil;

public class FTPUtilTests {

	/*
	 * @Test public void testListFolder() { String folderName =
	 * "/public_html/xorshop/product-images"; List<String> listKeys =
	 * FTPUtil.listFolder(folderName); listKeys.forEach(System.out::println); }/*

	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test-upload";
		String fileName = "xortech-beta.png";
		String filePath = "C:\\dev\\Xorshop Code\\images\\" + fileName;

		InputStream inputStream = new FileInputStream(filePath);

		FTPUtil.uploadFile(folderName, fileName, inputStream);
	}*/
  /*@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test";
		String fileName = "user-2.png";
		String filePath = "C:\\dev\\Xorshop Code\\images\\" + fileName;

		InputStream inputStream = new FileInputStream(filePath);

		FTPUtil.uploadFile(folderName, fileName, inputStream);
	}*/
	/*@Test
	public void testDeleteFile() {
		String fileName = "test/user-2.png";
		FTPUtil.deleteFile(fileName);
	}*/
	@Test
	public void testRemoveFolder() {
		String folderName = "product-images/103";
		FTPUtil.removeFolder(folderName);
	}

}
