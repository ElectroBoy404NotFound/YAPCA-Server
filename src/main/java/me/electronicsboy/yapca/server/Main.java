package me.electronicsboy.yapca.server;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	private HashMap<String, String> userData = new HashMap<String, String>();
	private File userDataFile;
	
	public Main() throws Exception {
		System.out.println("[INFO] Starting Server");
		System.out.println("[DEBUG] Reading Userdata...");
		userDataFile = new File("users.dat");
		if(!userDataFile.exists()) {
			System.out.println("[INFO] Userdata file does not exist. Creating a new file...");
			userDataFile.createNewFile();
		}
		try(Scanner udatas = new Scanner(userDataFile)) {
			while(udatas.hasNext()) {
				String dataLine = udatas.nextLine();
				System.out.println("[DEBUG] Read userdata line: " + dataLine);
				userData.put(dataLine.split(":")[0], dataLine.split(":")[1]);
			}
		}
		System.out.println("[DEBUG] Read Userdata...");
		Server server = new Server(8000);
		server.startServer(this);
		System.out.println("[INFO] Server has successfully been started.");
	}
	
	public static void main(String[] args) {
		try {
			new Main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getUserData() {
		return userData;
	}
	public void addUserData(String username, String password) {
		this.userData.put(username, password);
	}
	public boolean checkLogin(String username, String password) {
		return this.userData.get(username).equals(password);
	}
}
