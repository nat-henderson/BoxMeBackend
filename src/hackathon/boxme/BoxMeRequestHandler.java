package hackathon.boxme;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxMeRequestHandler {
	private List<Method> storageServiceApis;
	
	public BoxMeRequestHandler() {
		storageServiceApis = new ArrayList<Method>();
		storageServiceApis.addAll(Arrays.asList(
				StorageServiceHandler.class.getMethods()));
	}
	
	/**
	 * Handle the request and return the String that will be sent
	 * back to the caller.
	 * @return the JSON object returned.
	 */
	public String handle(BoxMeRequest request) {
		for (Method m : storageServiceApis) {
			if (m.getName().equals(request.requestType)) {
				return m.invoke(new StorageServiceHandler(), request.requestParameters);
			}
		}
	}
}