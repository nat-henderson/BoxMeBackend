package hackathon.boxme;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import hackathon.boxme.register.AccountNotSupportedException;
import hackathon.boxme.register.DropBoxAuth;
import hackathon.boxme.register.DropBoxCredentials;
import hackathon.boxme.register.RegisterAccount;

public class RegistrationRequestHandler {
	public void registerDropboxAccount(String facebookID, String uid, String accessToken,
			String secretKey) throws AccountNotSupportedException {
		DropBoxCredentials creds = new DropBoxCredentials("dropbox",
				uid, accessToken, secretKey);
		System.out.println(creds);
		RegisterAccount.main(facebookID, creds);
	}
}
