package me.thisorthat.sa.server;

public class CommandDispatcher {
	public static void dispatchCMD(String cmd, String server) {
		if(server.equalsIgnoreCase("%%all")) {
			System.out.println("[SendAll] Sending command to all servers: '" + cmd + "'");
			for(MCServer mc : ConnectionHandler.clients) {
				mc.getOut().println(cmd);
				System.out.println("[SendAll] Command '" + cmd + "' successfully sent to server '" + mc.getName() + "'");
			}
		} else {
		for(MCServer mc : ConnectionHandler.clients) {
			if(mc.getName().equalsIgnoreCase(server)) {
				mc.getOut().println(cmd);
				System.out.println("[SendAll] Command '" + cmd + "' successfully sent to server '" + mc.getName() + "'");
			}
		}
		}
	}
	
}
