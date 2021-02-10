package me.thisorthat.sa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ConnectionHandler extends Thread {
	public static ArrayList<MCServer> clients = new ArrayList<>();
	Socket soc;

	public ConnectionHandler(Socket soc) {
		this.soc = soc;
		start();
	}

	public void run() {
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			System.out.println("[SendAll] Waiting for server name transmit");
			String serverName = in.readLine();
			clients.add(new MCServer(new PrintWriter(soc.getOutputStream(), true), serverName));
			System.out.println("[SendAll] Recieved name from server '" + serverName + "'");
			while (true) {
				try {
					String cmd = in.readLine();
					System.out.println("[SendAll] Reading Data: '" + cmd + "' from server '" + serverName + "'");
					if (cmd.startsWith("exec:*:")) {
						String[] args = cmd.substring(7).split(";");
						if (args.length > 2) {
							String verifyHash = args[0];
							if (!verifyHash.equals(SendAllServer.pass)) {
								System.out.println("Invalid session! Passwords do not match! Kicking client '"
										+ serverName + "'!");
								break;
							}
							String commandString = args[2];
							String serverNameString = args[1];
							if (serverNameString.equalsIgnoreCase("$list")) {
								System.out.println("[SendAll] Sending Server List...");
								String serverToSendTo = commandString;
								StringBuilder sb = new StringBuilder();
								for (MCServer mc : clients) {
									sb.append(mc.getName());
									sb.append(" | ");
								}
								CommandDispatcher.dispatchCMD("^%$list-" + sb.toString(), serverToSendTo);

							} else {
								System.out.println("[SendAll] Executing server command '" + commandString + "' on '"
										+ serverNameString + "'");
								CommandDispatcher.dispatchCMD(commandString, serverNameString);
							}
						}
					}
				} catch (SocketException h) {
					System.out.println("[SendAll] Client server '" + serverName + "' has disconnected");

					break;
				}
			}
			interrupt();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
