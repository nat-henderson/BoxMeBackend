package hackathon.boxme;

import java.io.IOException;
import java.io.InputStream;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.WebAuthSession;

public class PutFileRequest implements Runnable {

	private AppKeyPair appKeyPair;
	private AccessTokenPair token;
	private FileCopyStream inputStream;
	private String fileKey;

	public PutFileRequest(String fileKey, FileCopyStream inputStream,
			AccessTokenPair token, AppKeyPair pair) {
		this.fileKey = fileKey;
		this.token = token;
		this.appKeyPair = pair;
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		WebAuthSession session = new WebAuthSession(appKeyPair,
				Session.AccessType.DROPBOX, token);
		DropboxAPI<?> client = new DropboxAPI<WebAuthSession>(session);
		try {
			client.putFile(fileKey, inputStream.getInputStream(),
					inputStream.getSize(), null, null);
			System.out.println("file put");
			inputStream.getInputStream().close();
		} catch (DropboxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
