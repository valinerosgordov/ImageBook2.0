package ru.minogin.core.server.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ru.minogin.core.client.exception.Exceptions;

public class Exec {
	public static final String HOST = "localhost";
	public static final int PORT = 10200;

	private String host;
	private int port;

	public Exec() {
		this(HOST, PORT);
	}

	public Exec(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void execute(String command) {
		try {
			Socket socket = new Socket(host, port);

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader.readLine();

			socket.close();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
