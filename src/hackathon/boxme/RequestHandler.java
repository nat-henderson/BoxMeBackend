package hackathon.boxme;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestHandler {
	
	public static void main (String[] args) {
		int socketNum = 9125;
		try {
			socketNum = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			// no one cares about that; use default.
		}
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(socketNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				Socket clientSocket = socket.accept();
				new Thread(new ConnectionHandler(clientSocket)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
