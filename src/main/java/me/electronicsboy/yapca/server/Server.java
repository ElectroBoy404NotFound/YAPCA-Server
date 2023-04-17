package me.electronicsboy.yapca.server;

public class Server extends ServerConnection implements Runnable {
	private Thread thread;
	private Main main;
	
	public Server(int port) {
		super(port);
	}

	public void startServer(Main main) {
		thread = null;
		thread = new Thread(this, "Server Thread");
		thread.start();
		this.main = main;
	}
	
	@Override
	public void run() {
		super.startServer(main);
	}
}
