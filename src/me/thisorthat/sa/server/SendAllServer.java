package me.thisorthat.sa.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SendAllServer extends Plugin {
	static boolean enabled = false;
	static int port;
	static Configuration config;
	public static String pass;

	Thread t1;
	
	public void onEnable() {
		try {
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File configFile = new File(getDataFolder(), "Config.yml");
			if(!configFile.exists()) {
				configFile.createNewFile();
				InputStream in = getResourceAsStream("Config.yml");
				OutputStream out = new FileOutputStream(configFile);
				ByteStreams.copy(in, out);
				in.close();
				out.close();
			}
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "Config.yml"));
			port = config.getInt("port");
			pass = config.getString("pass");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashed = digest.digest(pass.getBytes());
			pass = java.util.Base64.getEncoder().encodeToString(hashed);
		} catch (IOException e1) {
			System.err.println("Failed to initiate config");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		enabled = true;
		t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerSocket ssoc = new ServerSocket(port);
					getLogger().info("Listening on port " + port);
					while (enabled) {
						new ConnectionHandler(ssoc.accept());
						
						getLogger().info("Connected to client");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		
		
		
		

	}

	public void onDisable() {
		t1.interrupt();
		enabled = false;

	}
}
