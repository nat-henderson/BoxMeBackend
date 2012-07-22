package hackathon.boxme;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	 * @throws NoSuchMethodException 
	 */
	public String handle(BoxMeRequest request) throws NoSuchMethodException {
		for (Method m : storageServiceApis) {
			if (m.getName().equals(request.requestType)) {
				Object retVal = null;
				try {
					retVal = m.invoke(new StorageServiceHandler(), request.requestParameters);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e1) {
					e1.printStackTrace();
				}
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(retVal);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		throw new NoSuchMethodException();
	}
}