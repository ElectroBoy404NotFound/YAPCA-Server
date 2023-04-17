package me.electronicsboy.yapca.server;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ServerConnection {
	
	private ServerSocket serverSocket;	
	private int port;

	public ServerConnection(int port) {
		this.port = port;
	}
	
	protected void startServer(Main main) {
		try {
			serverSocket = new ServerSocket(this.port);
			while(true) 
				new Thread(() -> {
					try {
						new Client(serverSocket.accept(), main);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();
		} catch (IOException e) {e.printStackTrace();}
	}
}
