package hackathon.boxme.register;

public class Credentials {
	private String Type;
	private String UserId;
	
	public Credentials(String type, String UserId) {
		this.Type = type;
		this.UserId = UserId;
	}
	
	public String getType(){
		return Type;
	}
	
	public void setType(String type){
		this.Type = type;
	}
	
	
	public String getUserId(){
		return UserId;
	}
	
	public void setUserId(String UserId){
		this.UserId= UserId;
	}
}
