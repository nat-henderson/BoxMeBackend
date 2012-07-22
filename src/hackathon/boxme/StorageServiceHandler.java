package hackathon.boxme;

import java.util.List;

public interface StorageServiceHandler {
	/*
	 * Returns a list of file for the user given the 
	 * userId 
	 */
	public String getFileList(String userId);
	
	/*
	 * send the list of files from the user file list to list of users 
	 */
	public boolean putFiles(String senderUserId, List<String> files , List<String> receiverUserIds );

}
