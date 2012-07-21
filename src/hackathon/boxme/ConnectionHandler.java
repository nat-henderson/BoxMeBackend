package hackathon.boxme;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionHandler implements Runnable {
	Socket clientSocket = null;

	public ConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		BoxMeRequest request = null;
		try {
			request = mapper.readValue(clientSocket.getInputStream(),
					BoxMeRequest.class);
		} catch (IOException e) {
			try {
				this.clientSocket.getOutputStream().write(
						"Invalid Input.".getBytes());
				this.clientSocket.close();
			} catch (IOException e1) {
				// not much we can do.
			}
			return;
		}
		List<BoxMeRequestHandler> handlers = new ArrayList<BoxMeRequestHandler>();
		// handlers.add(YourClass.class);
		for (BoxMeRequestHandler handler : handlers) {
			if (handler.handles(request.requestType)) {
				try {
					clientSocket.getOutputStream().write(
							handler.handle(request).getBytes());
					break;
				} catch (IOException e) {
					// look for something else to handle this.
				}
			}
		}
	}

}
