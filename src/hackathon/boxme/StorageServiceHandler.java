package hackathon.boxme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StorageServiceHandler {
	/*
	 * Returns a list of file for the user given the 
	 * userId 
	 */
	String dropBoxType = "dropbox";
	StorageProvider storageProvider;
	int accountDeciderColumn = 0; // Keeping it 0 for now
	String dropBoxAccount = "dropbox";
	String testUserCreds = "97orcuffrgdgezb 7cn721muswzhhkp";
	int dropBoxIndex = 0;
	int userNameIndex = 1;
	
	public String getFileList(String userId){
		HashMap<String, String> accountCredentials = new HashMap<String, String>();
		// Call to persistence store to get the account info
		accountCredentials = getTestCreds(userId);
		System.out.println(accountCredentials);
		// Iterator through account credentials 
		Iterator it = accountCredentials.entrySet().iterator();
		String fileList = "";
		while(it.hasNext()){
			Map.Entry tokenPair = (Map.Entry) it.next();
			String accountKey = (String) tokenPair.getKey();
			String accountTokens = (String) tokenPair.getValue();
			
			
			if(accountKey.contains(dropBoxType)){
				//Make a new dropbox Storage Provider
				storageProvider = new DropboxStorageProvider(); 
				String[] accountTokensList = accountTokens.split("\n");
				for(String accountToken : accountTokensList){
					fileList += "dropbox"+ storageProvider.listAllFiles(accountToken) + " ";
				}		
			}
		}
		return fileList;
	}
	
	public DirectoryListing getFilesUnderPath(String userId, String path){
		DirectoryListing allFiles = null, fileList = null ;
		String directoryPath = "";
		HashMap<String, String> accountCredentials = new HashMap<String, String>();
		String accountKey, accountTokens;
		// Call to persistence store to get the account info
		accountCredentials = getTestCreds(userId);
		System.out.println(accountCredentials);
		// Iterator through account credentials 
			
		
		String accountType = path.split("/")[dropBoxIndex];
		String userName = path.split("/")[userNameIndex];
			 
			
		if(accountType.equals(dropBoxType)){
			//Make a new dropbox Storage Provider
			storageProvider = new DropboxStorageProvider();
			accountKey = accountType+userName;
			accountTokens = accountCredentials.get(accountKey);
			//String[] accountTokensList = accountTokens.split("\n");
			
			fileList = storageProvider.getFilesUnderPath(directoryPath, accountTokens);
			fileList.insertPrefix(accountType+userName);
		}
		return fileList;
	}
				
			
	
	/*
	 * send the list of files from the user file list to list of users 
	 */
	public boolean putFiles(String senderUserId, List<String> filestoSend , List<String> receiverUserIds ){
		
		HashMap<String, String> senderCredentials = new HashMap<String, String>();
		// Call to persistence store to get the account info
		senderCredentials = getTestCreds(senderUserId);
		Boolean transfer = false;
		
		for(String filetoSend: filestoSend){
			// Iterator through account credentials 
			// from the fileId decide which credential to use
			String accountType = accountToUse(filetoSend);
			
			Iterator senderCredsIt = senderCredentials.entrySet().iterator();			
			String fileList = "";
			while(senderCredsIt.hasNext()){
				Map.Entry tokenPair = (Map.Entry) senderCredsIt.next();
				String senderKey = (String) tokenPair.getKey();
				String senderTokens = (String) tokenPair.getValue();
				if(senderKey.equals(accountType)){
					// Now for each file have to go through receiver credentials 
					// and then call a copy if both are dropbox 
					// otherwise do a getFile to get a file stream
					// call a putFile to put a file stream 
					for(String receiverId : receiverUserIds){
						// Call the database to get the Credentials for receiverId
						HashMap<String, String> receiverCredentials = new HashMap<String, String>();
						
						receiverCredentials = getTestCreds(receiverId);
						Iterator receiverCredsIt = receiverCredentials.entrySet().iterator();
						while(receiverCredsIt.hasNext()){
							Map.Entry receiverTokenPair = (Map.Entry) receiverCredsIt.next();
							String receiverKey = (String) tokenPair.getKey();
							String receiverTokens = (String) tokenPair.getValue();
							if(senderKey.contains(dropBoxType) && receiverKey.contains(dropBoxType)){
								// if both accounts are Dropbox
								storageProvider = new DropboxStorageProvider();
								String[] fileNamesToSend = filetoSend.split("/");
								String fileNametoSend = "/";
								for(int i=1;i<fileNamesToSend.length;i++){
									fileNametoSend+=fileNamesToSend[i];
								}
								System.out.println(fileNametoSend);
								transfer = storageProvider.copyFile(fileNametoSend, senderTokens, receiverTokens);
							}
						}
					}
				}
				
			}
		}
				
		return transfer;
	}
	
	private String accountToUse(String fileId){
		String[] fileNameParts = fileId.split("/");
		String accountType = "";
		
		if(fileNameParts[accountDeciderColumn].equals(dropBoxAccount)){
			accountType = dropBoxType;
		}
		return accountType;
	}
	
	private HashMap<String, String> getTestCreds(String userName){
		HashMap<String, String> testDbCreds = new HashMap<String, String>();
		testDbCreds.put("dropbox", testUserCreds);
		
		return testDbCreds;
	}
	
	public static void main(String[] args){
		StorageServiceHandler storageServiceHandler = new StorageServiceHandler();
		String sender = "dummy";
		String receiver = "dummy";
		String file = "dropbox/urls.txt";
		//String fileList = storageServiceHandler.getFileList(sender);
		//System.out.println(fileList);
		List<String> receiverIds = new ArrayList<String>();
		receiverIds.add(receiver);
		List<String> filestoSend= new ArrayList<String>();
		filestoSend.add(file);
		storageServiceHandler.putFiles(sender, filestoSend, receiverIds);
	}

}
