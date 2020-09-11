package network;

import utility.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class NetworkSystem {
		
	private final NetworkReceptor receptor;
	
	private final Set<NetworkClient> clients = new HashSet<>();
	
	public NetworkSystem(int port) throws IOException {
		Logger.INFO.log("Binding to network");
		final ServerSocket ss = new ServerSocket(port);
		ss.setSoTimeout(1000); // Important so the NetworkReceptor thread can react to thread interruptions
		receptor = new NetworkReceptor(this, ss);
		Logger.INFO.log("Successfully bound to network");
	}
	
	/**
	 * Should only be called by the Network Receptor
	 */
	protected void addClient(NetworkClient client) {
		synchronized(clients) {
			clients.add(client);
		}
	}

	public void stop() {
		Logger.INFO.log("Detaching from network");

		try {
			receptor.close();
			receptor.interrupt();
			receptor.join();

			synchronized(clients) {
				for(NetworkClient client : clients) {
					client.close();
				}
			}
		} catch (IOException e) {
			Logger.ERROR.log("Error while closing a socket");
		} catch (InterruptedException e) {
			Logger.ERROR.log("Network thread was interrupted while waiting for receptor thread to exit");
			e.printStackTrace(Logger.ERROR.getPrintStream());
		}

		Logger.INFO.log("Successfully detached from network");
	}

}
