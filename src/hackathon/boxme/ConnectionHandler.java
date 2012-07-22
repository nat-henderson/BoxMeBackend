package hackathon.boxme;

import java.io.IOException;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionHandler implements Runnable {
	Socket clientSocket = null;

	public ConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			doWork();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	
	public void doWork() throws IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		BoxMeRequest request = null;
		try {
			request = mapper.readValue(clientSocket.getInputStream(),
					BoxMeRequest.class);
		} catch (IOException e) {
			clientSocket.getOutputStream().write(
					"Invalid Input.".getBytes());
			clientSocket.getOutputStream().flush();
			clientSocket.close();
			return;
		}
		BoxMeRequestHandler handler = new BoxMeRequestHandler();
		
		try {
			clientSocket.getOutputStream().write(
					handler.handle(request).getBytes());
		} catch (NoSuchMethodException e) {
			clientSocket.getOutputStream().write(e.toString().getBytes());
		} finally {
			clientSocket.getOutputStream().flush();
			clientSocket.close();
		}
	}
}
