package hackathon.boxme;

import hackathon.boxme.register.AccountNotSupportedException;
import hackathon.boxme.register.DropBoxCredentials;
import hackathon.boxme.register.RegisterAccount;

public class RegistrationRequestHandler {
	public void registerDropboxAccount(String facebookID, String accessToken,
			String secretKey) throws AccountNotSupportedException {
		DropBoxCredentials creds = new DropBoxCredentials("dropbox",
				facebookID, accessToken, secretKey);
		RegisterAccount.main(facebookID, creds);
	}
}
