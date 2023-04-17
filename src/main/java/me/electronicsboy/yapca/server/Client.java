package me.electronicsboy.yapca.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONObject;

public class Client {
	protected InputStreamReader in;
	protected BufferedReader bf;
	private PrintWriter pw;
	private Socket socket;
	private Main main;
	
	public Client(Socket socket, Main main) throws IOException {
		this.socket = socket;
		in = new InputStreamReader(socket.getInputStream());
		bf = new BufferedReader(in);
		pw = new PrintWriter(socket.getOutputStream());
		this.main = main;
		try (Scanner s = new Scanner(bf)) {
			while(true) 
				while(s.hasNext()) dataReceive(s.nextLine());
		}
	}

	private void dataReceive(String nextLine) {
		System.out.println("[INFO] Recieved data: " + nextLine);
		JSONObject jsonObject = new JSONObject(nextLine);
		String state = jsonObject.getString("state");
		System.out.println("[INFO] State: " + state);
		switch(state) {
			case "CONNECT":
				System.out.println("[DEBUG] Got Client CONNECT");
				break;
			case "LOGIN":
				String username = ((JSONObject) jsonObject.get("data")).getString("username");
				String password = ((JSONObject) jsonObject.get("data")).getString("password");
				System.out.println("[DEBUG] Got LOGIN packet. Username: \"" + username + "\", Password: \"" + password + "\"");
				login(username, password);
				break;
			default:
				System.out.println("[WARN] Unknown state \"" + state + "\" received from client \"" + socket.getInetAddress() + "\"");
		}
	}
	
	private void login(String username, String password) {
		boolean correctLogin = main.checkLogin(username, password);
		System.out.println("[DEBUG] Is login successful: " + correctLogin);
		JSONObject resendData = new JSONObject();
		JSONObject returnData_data = new JSONObject();
		if(correctLogin) {
			System.out.println("[DEBUG] Login successful!");
			System.out.println("[DEBUG] Sending back login packet...");
			resendData.put("status", "LOGIN_SUCCESS");
			returnData_data.put("username", username);
			returnData_data.put("password", password);
			resendData.put("data", returnData_data);
		} else {
			System.out.println("[DEBUG] Login incorrect!");
			System.out.println("[DEBUG] Sending back login packet...");
			resendData.put("status", "LOGIN_FAIL");
			returnData_data.put("username", username);
			returnData_data.put("password", password);
			resendData.put("data", returnData_data);
		}
		pw.print(resendData.toString());
		pw.flush();
	}

	public void end() throws Exception {
		pw.close();
		socket.close();
	}
}
