package hackathon.boxme;

import hackathon.boxme.utils.SimpleDBUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StorageServiceHandler {
	/*
	 * Returns a list of file for the user given the 
	 * userId 
	 */
	String dropBoxType = "dropbox";
	String S3Type = "S3";
	List<String> providerList = new ArrayList<String>();
	StorageProvider storageProvider;
	int accountDeciderColumn = 0; // Keeping it 0 for now
	String dropBoxAccount = "dropbox";
	String testUserCreds = "97orcuffrgdgezb 7cn721muswzhhkp";
	int dropBoxIndex = 0;
	int providerIndex = 0;
	int providerPathIndex = 1 ; // Hack
	int uidIndex = 1;
	int userNameIndex = 1;
	int startIndex = 2;
	
	public String getFileList(String userId){
		HashMap<String, String> accountCredentials = new HashMap<String, String>();
		// Call to persistence store to get the account info
		//accountCredentials = getTestCreds(userId);
		accountCredentials = SimpleDBUtils.getAttributes(userId);
		System.out.println(accountCredentials);
		// Iterator through account credentials 
		Iterator it = accountCredentials.entrySet().iterator();
		String fileList = "";
		while(it.hasNext()){
			Map.Entry tokenPair = (Map.Entry) it.next();
			String accountKey = (String) tokenPair.getKey();
			String accountTokens = (String) tokenPair.getValue();
			if(!checkKey(accountKey)){
				continue;
			}
			
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
		DirectoryListing allFiles = null, fileList = new DirectoryListing() ;
		String directoryPath = "";
		HashMap<String, String> accountCredentials = new HashMap<String, String>();
		String accountKey, accountTokens;
		int hierarchyLevel;
		// Call to persistence store to get the account info
		//accountCredentials = getTestCreds(userId);
		//System.out.println(accountCredentials);
		// Iterator through account credentials 
		String[] pathArray = path.split("/");	
		hierarchyLevel = pathArray.length;
		//System.out.println(hierarchyLevel);
		if(hierarchyLevel == 0){
			//Call the Data store and get a list of service providers
			List<String> serviceProviders = null, returnList = new ArrayList<String>();
			Set<String> returnSet  = new HashSet<String>();
			//serviceProviders = getFromDbMock();
			//Simple DB Call
			serviceProviders = SimpleDBUtils.getAttributeList(userId);
			
			for(String providerKeys: serviceProviders){
				String provider = providerKeys.split(" ")[providerIndex];
				if(providerList.contains(provider)){
					returnSet.add(provider);
				}
			}
			for(String files: returnSet){
				returnList.add(files);
			}
			fileList.setDirectories(returnList);
		}else if(hierarchyLevel == 2){
			//Call the Data store and get an account list for the service provider
			List<String> serviceProviders = null , returnList  = new ArrayList<String>();
			//serviceProviders = getFromDbMock();
			//Simple DB Call
			serviceProviders = SimpleDBUtils.getAttributeList(userId);
			
			String givenProvider = pathArray[providerPathIndex];
			for(String providerKeys: serviceProviders){
				String provider = providerKeys.split(" ")[providerIndex];
				String uid = providerKeys.split(" ")[uidIndex];
				if(provider.equals(givenProvider)){
					returnList.add(provider+"/"+uid);
				}
			}
			fileList.setDirectories(returnList);
		}else if(hierarchyLevel > 2){
			String accountType = path.split("/")[dropBoxIndex];
			String userName = path.split("/")[userNameIndex];
				 
				
			if(accountType.equals(dropBoxType)){
				//Make a new dropbox Storage Provider
				storageProvider = new DropboxStorageProvider();
				accountKey = accountType+" "+userName;
				//accountTokens = accountCredentials.get(accountKey);
				accountTokens = SimpleDBUtils.getAttribute(userId, accountKey);
				//String[] accountTokensList = accountTokens.split("\n");
				
				fileList = storageProvider.getFilesUnderPath(directoryPath, accountTokens);
				fileList.insertPrefix(accountType+userName);
			}
		}
		

		return fileList;
	}
				
			
	
	/*
	 * send the list of files from the user file list to list of users 
	 */
	public boolean putFiles(String senderUserId, List<String> filestoSend , List<String> receiverUserIds ){
		
		HashMap<String, String> senderCredentials = new HashMap<String, String>();
		StorageProvider receiverStorageProvider;
		// Call to persistence store to get the account info
		senderCredentials = getTestCreds(senderUserId);
		//Simple Db Call
		senderCredentials = SimpleDBUtils.getAttributes(senderUserId);
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
				if(!checkKey(senderKey)){
					continue;
				}
				
				if(senderKey.equals(accountType)){
					System.out.println(senderKey);
					// Now for each file have to go through receiver credentials 
					// and then call a copy if both are dropbox 
					// otherwise do a getFile to get a file stream
					// call a putFile to put a file stream 
					for(String receiverId : receiverUserIds){
						// Call the database to get the Credentials for receiverId
						HashMap<String, String> receiverCredentials = new HashMap<String, String>();
						
						//receiverCredentials = getTestCreds(receiverId);
						//Simple Db Call
						receiverCredentials = SimpleDBUtils.getAttributes(receiverId);
						
						Iterator receiverCredsIt = receiverCredentials.entrySet().iterator();
						while(receiverCredsIt.hasNext()){
							Map.Entry receiverTokenPair = (Map.Entry) receiverCredsIt.next();
							String receiverKey = (String) tokenPair.getKey();
							String receiverTokens = (String) tokenPair.getValue();
							if(!checkKey(receiverKey)){
								continue;
							}
							
							if(senderKey.contains(dropBoxType) && receiverKey.contains(dropBoxType)){
								// if both accounts are Dropbox
								
								storageProvider = new DropboxStorageProvider();
								String[] fileNamesToSend = filetoSend.split("/");
								String fileNametoSend = "/";
								for(int i=startIndex;i<fileNamesToSend.length;i++){
									fileNametoSend+=fileNamesToSend[i];
								}
								//System.out.println(fileNametoSend);
								transfer = storageProvider.copyFile(fileNametoSend, senderTokens, receiverTokens);
								break;
							}else if(senderKey.contains(dropBoxType) && receiverKey.contains(S3Type)){
								storageProvider = new DropboxStorageProvider();
								receiverStorageProvider = new S3StorageProvider();
				
								String[] fileNamesToSend = filetoSend.split("/");
								String fileNametoSend = "/";
								for(int i=startIndex;i<fileNamesToSend.length;i++){
									fileNametoSend+=fileNamesToSend[i];
								}
								
								FileCopyStream copyStream = storageProvider.getFile(fileNametoSend, senderTokens);
							
								receiverStorageProvider.putFile(fileNametoSend, copyStream, receiverTokens);
								
							}else if(senderKey.contains(S3Type) && receiverKey.contains(dropBoxType)){
								
								//storageProvider = new S3StorageProvider();
								receiverStorageProvider = new DropboxStorageProvider();
								
								String[] fileNamesToSend = filetoSend.split("/");
								String fileNametoSend = "/";
								for(int i=startIndex;i<fileNamesToSend.length;i++){
									fileNametoSend+=fileNamesToSend[i];
								}
								
								FileCopyStream copyStream = storageProvider.getFile(fileNametoSend, senderTokens);
							
								receiverStorageProvider.putFile(fileNametoSend, copyStream, receiverTokens);
								
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
		/*
		if(fileNameParts[accountDeciderColumn].equals(dropBoxAccount)){
			accountType = dropBoxType;
		}
		*/
		accountType = fileNameParts[providerIndex]+" "+ fileNameParts[uidIndex];
		return accountType;
	}
	
	private Boolean checkKey(String key){
		Boolean flag = false;
		for(String providers: providerList){
			if(key.contains(providers)){
				flag = true;
			}
		}
		return flag;
	}
	
	private HashMap<String, String> getTestCreds(String userName){
		HashMap<String, String> testDbCreds = new HashMap<String, String>();
		testDbCreds.put("dropbox 12345", testUserCreds);
		
		return testDbCreds;
	}
	
	private List<String> getFromDbMock(){
		List<String> accounts = new ArrayList<String>();
		accounts.add("dropbox 12345");
		accounts.add("dropbox 453");
		
		return accounts;
	}
	
	public static void main(String[] args){
		StorageServiceHandler storageServiceHandler = new StorageServiceHandler();
		storageServiceHandler.providerList.add("dropbox");
		storageServiceHandler.providerList.add("googleDrive");
		storageServiceHandler.providerList.add("S3");
		String sender = "vtest";
		String receiver = "vtest";
		String file = "dropbox/123/drivers.txt";
		//String fileList = storageServiceHandler.getFileList(sender);
		//System.out.println(fileList);
		List<String> receiverIds = new ArrayList<String>();
		receiverIds.add(receiver);
		List<String> filestoSend= new ArrayList<String>();
		filestoSend.add(file);
		storageServiceHandler.putFiles(sender, filestoSend, receiverIds);
		DirectoryListing allNames = storageServiceHandler.getFilesUnderPath("vtest", "/dropbox/");
		if(allNames.getDirectories()!=null){
			for(String dir: allNames.getDirectories()){
				System.out.println(dir);
			}
		}
		if(allNames.getFiles()!=null){
			for(String files: allNames.getFiles()){
				System.out.println(files);
			}
		}
	}

}
