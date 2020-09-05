package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import bus.MessageBus;
import config.Config;
import utility.Logger;

public class Main extends Thread {
	
	private final int port;
	private final int tickTimeMilliseconds;
	
	private final MessageBus messageBus = new MessageBus();
	private final HashMap<String, AbstractSystem> systems = new HashMap<>();
	private final ThreadPoolExecutor systemsExecutor;
	
	private boolean running = true;
	
	public Main() {
		this.port = Config.getAsInt("port");
		final int maxTps = Config.getAsInt("max_tps");
		tickTimeMilliseconds = 1000 / maxTps;
		
		Logger.INFO.log("-------------------------");
		Logger.INFO.log("Welcome to Creion Server!");
		
		super.setName("Creion Server Operator Thread");
		Logger.INFO.log("Operator Thread running as \"Creion Server Operator Thread\"");
		Logger.INFO.log("Loading systems:");
		systems.put("entity_system", new EntitySystem(messageBus));
		// TODO dummy system is just for scheduler debugging
		final int DUMMY_COUNT = 2;
		for(int i = 0; i < DUMMY_COUNT; i++) {
			final DummySystem dummySystem = new DummySystem(messageBus, 30);
			systems.put("dummy_" + i, dummySystem);
		}
		Logger.INFO.log("Finished loading systems");
		
		final int threadPoolSize = Config.getAsInt("thread_pool_size");
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
				// Calculate how much time each system gets to run:
				// 100ms, 2 threads, 2 systems -> 100ms per system
				// 100ms, 1 thread, 2 systems -> 50ms per system
				// 100ms, 2 threads, 1 system -> 100ms per system
				final int blockCount = (int) (Math.max(1, Math.ceil(systems.size() / systemsExecutor.getMaximumPoolSize())));
				final int delta = tickTimeMilliseconds / blockCount;
				Logger.INFO.log("Simulating in " + blockCount + " block(s) @" + delta + "ms");
				// Assign a number of systems to each block equal to the number of threads
				final ArrayList<HashSet<AbstractSystem>> blocks = new ArrayList<>();
				final CountDownLatch[] barriers = new CountDownLatch[blockCount];
				final Iterator<AbstractSystem> it = systems.values().iterator();
				int remaining = systems.values().size();
				for(int b = 0; b < blockCount; b++) {
					final HashSet<AbstractSystem> block = new HashSet<>();
					// Set up a barrier for this batch to synchronize on.
					// The party count is equals the size of the batch
					final CountDownLatch barrier = new CountDownLatch(
							remaining > systemsExecutor.getMaximumPoolSize() 
							? systemsExecutor.getMaximumPoolSize() : remaining);
					barriers[b] = barrier;
					for(int i = 0; i < systemsExecutor.getMaximumPoolSize(); i++) {
						if(it.hasNext()) {
							final AbstractSystem system = it.next();
							system.setBarrier(barrier);
							block.add(system);
							remaining--;
						} else {
							break;
						}
					}
					blocks.add(block);
				}
				// Queue up block by block and wait for delta milliseconds
				for(int b = 0; b < blockCount; b++) {
					// Aquire block start time
					final long startTime = System.currentTimeMillis();
					
					final HashMap<String, Future<?>> futures = new HashMap<>();
					for(AbstractSystem system : blocks.get(b)) {
						futures.put(system.toString(), systemsExecutor.submit(system));
					}
					// Sync on this batches barrier
					barriers[b].await(delta * 10, TimeUnit.MILLISECONDS);
					boolean allDone = true;
					for(Entry<String, Future<?>> future : futures.entrySet()) {
						if(!future.getValue().isDone()) {
							// Try to get the system to stop.
							future.getValue().cancel(true);
							Logger.ERROR.log("System " + future.getKey() + " is simulating for more than 10x as long as it should"
									+ "and was requested to stop");
							allDone = false;
							break;
						}
					}
					if(!allDone) {
						Logger.ERROR.log("A system has broken the batch synchronization barrier\n"
								+ "Things may go wrong after here, so the server will shut down for security");
						running = false;
						break;
					} else {
						final long blockTime = System.currentTimeMillis() - startTime;
						Logger.INFO.log("Block ended after " + blockTime + "ms");
					}
				}
				
				// Debug thing so the server is up for at least 10s before shutting down again
				Logger.INFO.log("Running");
				Thread.sleep(5000);
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
			Logger.INFO.log("Stopping system:" + system.getKey());
			system.getValue().stop();
		}
	}
	
	public static void main(String[] args) {
		
		try {
			final String configPath = args[0];
			Config.load(configPath);
			
			final Main server = new Main();
			server.start();
			Logger.INFO.log("Creion Server is now running");
		} catch (ArrayIndexOutOfBoundsException e) {
			Logger.ERROR.log("Missing startup arguments!\n"
					+ "Please supply the path to the config file");
		}
		
	}
}
