package com.xorshop.admin.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	private static final Logger LOGGER=LoggerFactory.getLogger(FileUploadUtil.class);
	public static void saveFile(String uploaddirectory, String name, MultipartFile mpf) throws IOException {
		Path uploadpath = Paths.get(uploaddirectory);
		if (!Files.exists(uploadpath)) {
			Files.createDirectories(uploadpath);
		}

		try (InputStream is = mpf.getInputStream()) {
			Path filepath = uploadpath.resolve(name);
			Files.copy(is, filepath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ex) {
			throw new IOException("Impossible de sauvegarder le fichier: " + name, ex);
		}

	}


	public static void cleanDir(String directory) throws IOException {
		Path drp = Paths.get(directory);
		try {
			Files.list(drp).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						LOGGER.error("Impossible de supprimer le fichier" + file);
						//System.out.println("Impossible de supprimer le fichier" + file);
					}

				}
			});

		} catch (IOException ex) {
			LOGGER.error("Impossible d'accéder au chemin : " + drp);
			//throw new IOException("Impossible d'accéder au chemin : " + drp);
		}
	}

	public static void DeleteFileDirectory(String directory, String filePath) {
		// TODO Auto-generated method stub
		Path drp = Paths.get(directory);
		String filename = Paths.get(filePath).getFileName().toString();
		System.err.println("MOMI reper "+directory+" filname"+filename);
		try {
			File doomedFile = new File (directory, filename);
			doomedFile.delete();

		} catch (Exception ex) {
			LOGGER.error("Impossible d'accéder au chemin : " + drp);
			//throw new IOException("Impossible d'accéder au chemin : " + drp);
		}
		
	}

	public static void removeDir(String dir)  {
		try {
			cleanDir(dir);
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Impossible de supprimer le repertoire"+dir);
		}
		
	}

}
