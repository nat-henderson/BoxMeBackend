package hackathon.boxme;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionHandler implements Runnable {
	Socket clientSocket = null;

	public ConnectionHandler(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		System.out.println(this.clientSocket.isClosed());
	}

	@Override
	public void run() {
		try {
			doWork();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doWork() throws IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		BoxMeRequest request = null;
		System.out.println(clientSocket.isClosed());
		try {
			StringBuilder sb = new StringBuilder("");
			String string = "initialSet";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			while (!string.equals("")) {
				System.out.println(string);
				string = reader.readLine();
				sb.append(string);
			}

			System.out.println(sb.toString());
			request = mapper.readValue(sb.toString(), BoxMeRequest.class);
			System.out.println(clientSocket.isClosed());
		} catch (IOException e) {
			e.printStackTrace();
			clientSocket.getOutputStream().write("Invalid Input.".getBytes());
			clientSocket.getOutputStream().flush();
			clientSocket.close();
			return;
		}
		BoxMeRequestHandler handler = new BoxMeRequestHandler();
		System.out.println(clientSocket.isClosed());
		try {
			clientSocket.getOutputStream().write(
					handler.handle(request).getBytes());
			clientSocket.getOutputStream().flush();
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			clientSocket.getOutputStream().write(e.toString().getBytes());
			clientSocket.getOutputStream().close();
		}
	}
}
