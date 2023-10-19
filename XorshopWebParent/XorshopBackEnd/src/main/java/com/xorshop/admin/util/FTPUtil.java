package com.xorshop.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtil.class);

	private static final String SERVER, PORT, USER, PASS, FOLDER;

	static {

		SERVER = System.getenv("FTP_SERVER");
		PORT = System.getenv("FTP_PORT");
		USER = System.getenv("FTP_USER");
		PASS = System.getenv("FTP_PASS");
		FOLDER = System.getenv("FTP_FOLDER_ROOT_NAME");

		LOGGER.info("FTPUtil | FTP_SERVER : " + SERVER);
	}

	private static FTPClient ConnectFTP() {
		LOGGER.info("FTPClient | ConnectFTP | URL : " + SERVER);
		FTPClient client = new FTPClient();
		try {
			LOGGER.info("FTPClient | ConnectFTP | trying to connect... ");
			client.connect(SERVER, Integer.parseInt(PORT));
			client.login(USER, PASS);
			client.enterLocalPassiveMode();
			LOGGER.info("FTPClient | ConnectFTP | connected successfully ");
		} catch (Exception e) {
			LOGGER.error("FTPClient | ConnectFTP | Could not connect to ftp " + SERVER, e);

		}
		return client;
	}

	private static void DisconnectFTP(FTPClient client) {
		if (client != null && client.isConnected()) {
			try {
				LOGGER.info("FTPClient |  DisonnectFTP | trying to disconnect... ");

				client.logout();
				client.disconnect();
				LOGGER.info("FTPClient |  DisonnectFTP | disconnected successfully from the server " + SERVER);
			} catch (Exception e) {
				LOGGER.error("FTPClient |  DisonnectFTP | Could not disconnecting from ftp " + SERVER, e);
			}
		}

	}

	public static List<String> listFolder(String folderName) {
		LOGGER.info("FTPClient | listFolder is started");
		List<String> listKeys = new ArrayList<>();
		FTPClient client = ConnectFTP();
		LOGGER.info("FTPClient | listFolder | client : " + client.toString());
		try {
			client.changeWorkingDirectory(folderName);
			String[] files = client.listNames();
			for (String file : files) {
				if (!file.equals(".") && !file.equals("..")) {
					listKeys.add(file);
				}

			}
			LOGGER.info("FTPClient | listFolder is successfully over");
			return listKeys;
		} catch (Exception e) {
			LOGGER.error("FTPClient |  listFolder | Could not lsting the folder " + folderName, e);
			return null;
		} finally {
			DisconnectFTP(client);
		}
	}

	public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
		LOGGER.info("FTPClient | uploadFile is started");

		FTPClient client = ConnectFTP();
		LOGGER.info("FTPClient | uploadFile | client : " + client.toString());
		try {
			client.setFileType(FTP.BINARY_FILE_TYPE);

			// Check if the directory exists, and create it if it doesn't.
			if (!client.changeWorkingDirectory("/public_html/" + FOLDER + "/" + folderName)) {
				LOGGER.info("FTPClient | uploadFile | Directory does not exist. Creating directory: " + folderName);
				if (!client.makeDirectory("/public_html/" + FOLDER + "/" + folderName)) {
					LOGGER.error("FTPClient | uploadFile | Failed to create directory: " + folderName);
					return;
				}
				LOGGER.info("FTPClient | uploadFile | Directory created successfully: " + folderName);
				if (!client.changeWorkingDirectory("/public_html/" + FOLDER + "/" + folderName)) {
					LOGGER.error("FTPClient | uploadFile | Failed to change working directory to: " + folderName);
					return;
				}
			}

			LOGGER.info("FTPClient | uploadFile | request : " + client.toString());

			LOGGER.info("FTPClient | uploadFile | inputStream : " + inputStream.toString());
			if (client.storeFile(fileName, inputStream)) {
				LOGGER.info("FTPClient | uploadFile is successfully over");
			} else {
				LOGGER.error("FTPClient | uploadFile | Failed to upload the file: " + fileName);
			}
		} catch (IOException e) {
			LOGGER.error("FTPClient |  uploadFile | Could not upload " + fileName + " to the server", e);
		} finally {
			DisconnectFTP(client);
			try {
				inputStream.close();
			} catch (IOException e) {
				LOGGER.error("FTPClient |  uploadFile | Error while closing the input stream", e);
			}
		}
	}

	public static void deleteFile(String fileToDelete) {
		LOGGER.info("FTPClient | deleteFile is started");

		FTPClient client = ConnectFTP();
		LOGGER.info("FTPClient | deleteFile | client : " + client.toString());
		try {
			LOGGER.info("FTPClient | deleteFile | remove file from /public_html/" + FOLDER + "/" + fileToDelete
					+ " in progress ");

			boolean deleted = client.deleteFile("/public_html/" + FOLDER + "/" + fileToDelete);
			if (deleted) {
				LOGGER.info("FTPClient | deleteFile | The file was deleted successfully.");
			} else {
				LOGGER.info("FTPClient | deleteFile | Could not delete the  file, it may not exist. ");
			}

			LOGGER.info("FTPClient | deleteFile is successfully over");
		} catch (Exception e) {
			LOGGER.error("FTPClient |  deleteFile | Oh no, there was an error: " + e.getMessage());
		} finally {
			DisconnectFTP(client);
		}
	}

	public static void removeFolder(String folderName) {

		LOGGER.info("FTPClient | deleteFolder is started");

		FTPClient client = ConnectFTP();
		LOGGER.info("FTPClient | deleteFolder | client : " + client.toString());
		try {
			LOGGER.info("FTPClient | deleteFolder | remove folder from /public_html/" + FOLDER + "/" + folderName
					+ " in progress ");

			FTPUtil.removeDirectory(client, "/public_html/" + FOLDER + "/", folderName);

			LOGGER.info("FTPClient | deleteFolder is successfully over");
		} catch (Exception e) {
			LOGGER.error("FTPClient | deleteFolder | Oh no, there was an error: " + e.getMessage());
		} finally {
			DisconnectFTP(client);
		}
	}

	/**
	 * Removes a non-empty directory by delete all its sub files and sub directories
	 * recursively. And finally remove the directory.
	 */
	public static void removeDirectory(FTPClient client, String parentDir, String currentDir) throws IOException {
		LOGGER.info("FTPClient | removeFolder is started");

		LOGGER.info("FTPClient | removeFolder | client : " + client.toString());
		try {

			String dirToList = parentDir;
			if (!currentDir.equals("")) {
				dirToList += "/" + currentDir;
			}

			FTPFile[] subFiles = client.listFiles(dirToList);

			if (subFiles != null && subFiles.length > 0) {
				for (FTPFile aFile : subFiles) {
					String currentFileName = aFile.getName();
					if (currentFileName.equals(".") || currentFileName.equals("..")) {
						// skip parent directory and the directory itself
						continue;
					}
					String filePath = parentDir + "/" + currentDir + "/" + currentFileName;
					if (currentDir.equals("")) {
						filePath = parentDir + "/" + currentFileName;
					}

					if (aFile.isDirectory()) {
						// remove the sub directory
						removeDirectory(client, dirToList, currentFileName);
					} else {
						// delete the file
						boolean deleted = client.deleteFile(filePath);
						if (deleted) {
							LOGGER.info("FTPClient | removeFolder DELETED the file: " + filePath);
						} else {
							LOGGER.info("FTPClient | removeFolder CANNOT delete the file: " + filePath);
						}
					}
				}

				// finally, remove the directory itself
				boolean removed = client.removeDirectory(dirToList);
				if (removed) {
					LOGGER.info("FTPClient | removeFolder REMOVED the directory: " + dirToList);
				} else {
					LOGGER.info("FTPClient | removeFolder CANNOT remove the directory: " + dirToList);
				}
			}
		} catch (Exception e) {
			LOGGER.error("FTPClient |  removeFolder | Could not remove the folder " + currentDir, e);
		} 
	}

}
