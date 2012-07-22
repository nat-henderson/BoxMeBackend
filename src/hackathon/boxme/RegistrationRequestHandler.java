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
		RegisterAccount.main(facebookID, creds);
	}
	public String getUrl(){
		ObjectMapper mapper = new ObjectMapper();
		String authUrl = DropBoxAuth.getUrl();
		String jsonReturn = null;
		try {
			jsonReturn = mapper.writeValueAsString(authUrl);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonReturn;
	}
	
	public void getTokens(String facebookID){
		DropBoxAuth.getTokens(facebookID);
	}
}
