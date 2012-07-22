package hackathon.boxme.register;

public class Credentials {
	private String Type;
	private String FacebookId;
	
	public Credentials(String type, String FacebookId) {
		this.Type = type;
		this.FacebookId = FacebookId;
	}
	
	public String getType(){
		return Type;
	}
	
	public void setType(String type){
		this.Type = type;
	}
	
	
	public String getFacebookId(){
		return Type;
	}
	
	public void setFacebook(String type){
		this.Type = type;
	}

	public void setFacebookId(String facebookId) {
		FacebookId = facebookId;
	}
}
