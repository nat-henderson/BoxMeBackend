package hackathon.boxme;

import java.net.Socket;

public class ConnectionHandler implements Runnable {
	Socket clientSocket = null;
	
	public ConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
