package me.thisorthat.sa.server;

import java.io.PrintWriter;
import java.net.Socket;

public class MCServer {
	String name;
	PrintWriter out;
	public MCServer(PrintWriter out, String name) {
		this.name = name;
		this.out = out;
	}
	public String getName() {
		return name;
	}
	public PrintWriter getOut() {
		return out;
	}
}
