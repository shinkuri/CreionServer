package nomm;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.Sequence;

import bus.MessageBus;
import utility.Logger;

public class Main extends Thread {
	
	private final int port;
	private final int tickTimeMilliseconds;
	
	private final MessageBus messageBus = new MessageBus();
	private final HashMap<String, AbstractSystem> systems = new HashMap<>();
	private final ThreadPoolExecutor systemsExecutor;
	
	private boolean running = true;
	
	public Main(int port, int threadPoolSize, int tickTimeMilliseconds) {
		this.port = port;
		this.tickTimeMilliseconds = tickTimeMilliseconds;
		
		Logger.INFO.log("-------------------------");
		Logger.INFO.log("Welcome to Creion Server!");
		
		super.setName("Creion Server Operator Thread");
		Logger.INFO.log("Operator Thread running as \"Creion Server Operator Thread\"");
		// TODO Add default systems
		
		for(Entry<String, AbstractSystem> system : systems.entrySet()) {
			Logger.INFO.log("Registering default system to message bus: " + system.getKey());
			messageBus.registerRecipient(system.getValue());
		}
		
		Logger.INFO.log("Setting up System Thread Pool with " + threadPoolSize + " threads");
		systemsExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);
		systemsExecutor.allowCoreThreadTimeOut(false);
		
		Logger.INFO.log("Initialization complete");
		Logger.INFO.log("-----------------------");
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted() && running) {
				Logger.INFO.log("Running");
				Thread.sleep(10000);
				running = false;
			}			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			shutdown();
			Logger.INFO.log("Good bye!");
		}
	}
	
	/**
	 * Awaits termination of all running system simulations,
	 * shuts them down, and then returns.
	 */
	private void shutdown() {
		Logger.INFO.log("--------------------------");
		Logger.INFO.log("Starting Shudown Sequence:");
		Logger.INFO.log("Waiting for all systems to finish execution or until 1000ms have passed");
		systemsExecutor.shutdown();
		try {
			systemsExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Logger.ERROR.log("Operator Thread was interupted while waiting for systems to finish execution");
			e.printStackTrace(Logger.ERROR.getPrintStream());
		}
		if(!systemsExecutor.isTerminated()) {
			Logger.INFO.log("At least one system failed to terminate in time");
		}
		Logger.INFO.log("Releasing resources held by systems");
		for(Entry<String, AbstractSystem> system : systems.entrySet()) {
			Logger.INFO.log("Unregistering " + system.getKey() + " from message bus");
			messageBus.unregisterRecipient(system.getValue());
			Logger.INFO.log("Stopping system");
			system.getValue().stop();
		}
	}
	
	/**
	 * Main method to start the server. Arguments are:</br>
	 * - The port number on which the server should listen on.</br>
	 * - Number of threads that the server should use for simulation.</br>
	 * - Tick time in milliseconds. Each system will try to simulate 
	 * 	a single tick within this time period. If the system finishes 
	 * 	within the given time, it will be scheduled for the next tick
	 * 	immediately. Otherwise the issue will be logged and the system
	 * 	will be scheduled as soon as possible in an effort to catch up.
	 * @param args
	 * 			portNumber, threadPoolSize, tickTime
	 */
	public static void main(String[] args) {
		
		try {
			final int port = Integer.parseInt(args[0]);
			final int threadPoolSize = Integer.parseInt(args[1]);
			final int tickTime = Integer.parseInt(args[2]);
			
			final Main server = new Main(port, threadPoolSize, tickTime);
			server.start();
			Logger.INFO.log("Creion Server is now running");
		} catch (ArrayIndexOutOfBoundsException e) {
			Logger.ERROR.log("An argument count of less than three was supplied. "
					+ "Please supply the port number, thread pool size, and tick time in milliseconds");
		} catch (NumberFormatException e) {
			Logger.ERROR.log("At least one argument could not be parsed as an integer.");
		}
				
		//final NetworkSystem networkSystem = new NetworkSystem(messageBus, port);
		//networkSystem.stop();
		/*
		try (ECDatabase ecDatabase = new ECDatabase("jdbc:sqlite:data/db.db")) {
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
}
