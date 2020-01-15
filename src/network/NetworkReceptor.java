package network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import utility.Logger;

public class NetworkReceptor implements Runnable, Closeable {
	
	private final NetworkSystem networkSystem;
	private final ServerSocket serverSocket;
	
	public NetworkReceptor(NetworkSystem networkSystem, ServerSocket serverSocket) {
		this.networkSystem = networkSystem;
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				final Socket cs = serverSocket.accept();
				networkSystem.addClient(new NetworkClient(cs));
			} catch (SecurityException e) {
				Logger.ERROR.log("Security Manager prevented connection: " + e.getLocalizedMessage());
			} catch (SocketTimeoutException e) {
				// Just ignore this
			} catch (SocketException e) {
				Logger.INFO.log("Server Socket has been closed by another thread while waiting for clients");
			} catch (IOException e) {
				Logger.ERROR.log("Error while waiting for client connection");
			}
		}
	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
	}

}
