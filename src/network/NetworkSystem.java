package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import bus.Message;
import bus.MessageBus;
import nomm.AbstractSystem;
import utility.Logger;

public class NetworkSystem extends AbstractSystem {
		
	private NetworkReceptor receptor;
	private Thread receptorThread;
	
	private final HashSet<NetworkClient> clients = new HashSet<>();
	
	public NetworkSystem(MessageBus messageBus, int port) {
		super(messageBus);
		
		Logger.INFO.log("Starting Network System");		
				
		try {
			final ServerSocket ss = new ServerSocket(port);
			ss.setSoTimeout(1000); // Important so the NetworkReceptor thread can react to thread interruptions
			receptor = new NetworkReceptor(this, ss);
		} catch (IOException e) {
			Logger.ERROR.log("Error while binding Server Socket: " + e.getLocalizedMessage());
		}
		
		receptorThread = new Thread(receptor);
		receptorThread.start();
		
		Logger.INFO.log("Successfully started Network System");
	}
	
	/**
	 * Should only be called by the Network Receptor
	 */
	public void addClient(NetworkClient client) {
		synchronized(clients) {
			clients.add(client);
		}
	}
	
	/**
	 * Do system specific clean up
	 */
	@Override
	public void stop() {
		Logger.INFO.log("Stopping Network System...");
		
		super.stop();
		
		try {
			receptor.close();
			receptorThread.interrupt();
			receptorThread.join();
			
			synchronized(clients) {
				for(NetworkClient client : clients) {
					client.close();
				}				
			}
		} catch (IOException e) {
			Logger.ERROR.log("Error while closing a Socket");
		} catch (InterruptedException e) {
			Logger.ERROR.log("Network System Thread was interrupted while waiting for Server Socket Thread to die");
			e.printStackTrace(Logger.ERROR.getPrintStream());
		}
		
		Logger.INFO.log("Sucessfully shut down Network System");
	}
	
	@Override
	protected void update() {
		
	}

	@Override
	protected void processMessages() {
		final Set<Message> messages =  super.messageBus.receive(this);
		// Do stuff
	}
	
}
