package hackathon.boxme;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.WebAuthSession;

public class DropboxStorageProvider implements StorageProvider {

	private AppKeyPair appKeyPair = new AppKeyPair(
			"u4d52lnoqpoqztk", "58xjxsb08ybg584");

	private AccessTokenPair processCredentials(String credentials) {
		String[] credSplit = credentials.split(" ");
		AccessTokenPair access = new AccessTokenPair(credSplit[0], credSplit[1]);
		return access;
	}

	@Override
	public void putFile(String fileKey, FileCopyStream stream, String receiverCredentials) {
		AccessTokenPair token = processCredentials(receiverCredentials);
		AppKeyPair pair = this.appKeyPair;
		System.out.println("putFile");
		new Thread(new PutFileRequest(fileKey, stream, token, pair)).start();
	}

	@Override
	public FileCopyStream getFile(String fileKey, String senderCredentials) {
		AccessTokenPair token = processCredentials(senderCredentials);
		WebAuthSession session = new WebAuthSession(appKeyPair,
				Session.AccessType.DROPBOX, token);
		System.out.println("getfile");
		DropboxAPI<?> client = new DropboxAPI<WebAuthSession>(session);
		try {
			FileCopyStream stream = new FileCopyStream();
			stream.setInputStream(client.getFileStream(fileKey, null));
			stream.setSize(client.metadata(fileKey, 1, null, false, null).bytes);
			return stream;
		} catch (DropboxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean copyFile(String fileKey, String senderCredentials,
			String receiverCredentials) {
		try {
			AccessTokenPair sender = processCredentials(senderCredentials);
	        // Connect to the <source> UID and create a copy-ref.
	        WebAuthSession sourceSession = new WebAuthSession(this.appKeyPair, Session.AccessType.DROPBOX, sender);
	        DropboxAPI<?> sourceClient = new DropboxAPI<WebAuthSession>(sourceSession);
	        DropboxAPI.CreatedCopyRef cr = sourceClient.createCopyRef(fileKey);
	
        	AccessTokenPair receiver = processCredentials(receiverCredentials);
	        WebAuthSession targetSession = new WebAuthSession(this.appKeyPair, Session.AccessType.DROPBOX, receiver);
	        DropboxAPI<?> targetClient = new DropboxAPI<WebAuthSession>(targetSession);
	        targetClient.addFromCopyRef(cr.copyRef, fileKey);
	        return true;
		} catch (DropboxException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> listAllFiles(String credentials) {
		List<String> files = new LinkedList<String>();
		AccessTokenPair creds = processCredentials(credentials);
        // Connect to the <source> UID and create a copy-ref.
        WebAuthSession session = new WebAuthSession(this.appKeyPair, Session.AccessType.DROPBOX, creds);
        DropboxAPI<?> client = new DropboxAPI<WebAuthSession>(session);
        addFilesToList(client, "", files);
        return files;
	}

	private void addFilesToList(DropboxAPI<?> client, String parentDir, List<String> files) {
		Entry parent = null;
		try {
			parent = client.metadata(parentDir, 25000, null, true, null);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (parent != null && parent.contents != null) {
			for (Entry child : parent.contents) {
				System.out.println(child.path + " " + child.isDir);
				if (child.isDir) {
					addFilesToList(client, child.path, files);
				} else {
					files.add(child.parentPath() + "/" + child.fileName());
				}
			}
		}
	}
	
	public static void main(String[] args) throws DropboxException{
		DropboxStorageProvider dsp = new DropboxStorageProvider();
/*        WebAuthSession was = new WebAuthSession(dsp.appKeyPair, Session.AccessType.APP_FOLDER);
        WebAuthSession.WebAuthInfo info = was.getAuthInfo();
        System.out.println("1. Go to: " + info.url);
        System.out.println("2. Allow access to this app.");
        System.out.println("3. Press ENTER.");

        try {
            while (System.in.read() != '\n') {}
        }
        catch (IOException ex) {
        	
        }

     //    This will fail if the user didn't visit the above URL and hit 'Allow'.
        String uid = was.retrieveWebAccessToken(info.requestTokenPair);
        AccessTokenPair accessToken = was.getAccessTokenPair();
        System.out.println(accessToken.key + " " + accessToken.secret);
*/
		FileCopyStream file = dsp.getFile("/urls.txt", "97orcuffrgdgezb 7cn721muswzhhkp");
		dsp.putFile("urlscopy.txt", file, "97orcuffrgdgezb 7cn721muswzhhkp");
	}
}
