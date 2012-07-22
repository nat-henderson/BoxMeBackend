package hackathon.boxme.register;

public class DropBoxCredentials extends Credentials {
	private String access_token;
	private String secret_key;

	public String getAccessToken(){
		return access_token;
	}
	
	public void setAccessToken(String access_token){
		this.access_token = access_token;
	}
	
	public String getSecretKey(){
		return secret_key;
	}
	
	public void setSecretKey(String secret){
		this.secret_key = secret;
	}
	
	public DropBoxCredentials(String type, String userId, String access_token, String secret_key) {
		super(type,userId);
		this.access_token = access_token;
		this.secret_key = secret_key;
	}

	@Override
	public String toString() {
		return "DropBoxCredentials [access_token=" + access_token
				+ ", secret_key=" + secret_key + ", getType()=" + getType()
				+ ", getFacebookId()=" + getFacebookId() + "]";
	}

}
