package hackathon.boxme;

import java.io.InputStream;
import java.util.List;

public interface StorageProvider {
	
	/*
	 * Copy the file input stream to list of users given their credentials
	 */
	public void putFile(String fileKey, FileCopyStream stream, String receiverCredentials);
	
	/*
	 *  Get the file output stream to list of users given sender credentials
	 *  fileKey is an unique fileId 
	 */
	public FileCopyStream getFile(String fileKey, String senderCredentials);
	
	/*
	 * 
	 */
	public DirectoryListing getFilesUnderPath(String path, String credentials);
	
	/*
	 *  copyFiles between a sender and a receiver
	 *  fileKey is an unique fileId 
	 */
	public boolean copyFile(String fileKey, String senderCredentials, String receiverCredentials);
	
	/*
	 * Return a list of files given credentials
	 */
	public List<String> listAllFiles(String credentials);
	
}
