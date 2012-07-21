package hackathon.boxme;

public interface BoxMeRequestHandler {

	/**
	 * Does this request handler handle this type of request?
	 * @param requestName name of the type of request.
	 * @return whether the request can be handled.
	 */
	public boolean handles(String requestName);
	
	/**
	 * Handle the request and return the String that will be sent
	 * back to the caller.
	 * @return the JSON object returned.
	 */
	public String handle(BoxMeRequest request);
	
}
