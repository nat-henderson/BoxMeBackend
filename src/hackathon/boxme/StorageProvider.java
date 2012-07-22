package hackathon.boxme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public interface StorageProvider {
	
	/*
	 * Copy the file input stream to list of users given their credentials
	 */
	public void putFile(FileInputStream inputStream, List<String> receiverCredentials);
	
	/*
	 *  Get the file output stream to list of users given sender credentials
	 *  fileKey is an unique fileId 
	 */
	public FileOutputStream getFile(String fileKey, String senderCredentials);
	
	
	/*
	 *  copyFiles between a sender and a receiver
	 *  fileKey is an unique fileId 
	 */
	public boolean copyFile(String fileKey, String senderCredentials, List<String> receiverCredentials);
	
	/*
	 * Return a list of files given credentials
	 */
	public List<String> listAllFiles(String credentials);
	
}
