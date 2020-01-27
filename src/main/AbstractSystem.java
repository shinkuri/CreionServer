package main;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import bus.MessageBus;

public abstract class AbstractSystem implements Runnable {
	
	private final UUID uuid = UUID.randomUUID();
	
	protected final MessageBus messageBus;
	
	private CountDownLatch barrier;
	
	public AbstractSystem(MessageBus messageBus) {
		this.messageBus = messageBus;
		
		messageBus.registerRecipient(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof AbstractSystem) {
			final AbstractSystem oa = (AbstractSystem) o;
			return oa.uuid.equals(this.uuid);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
	
	public void setBarrier(CountDownLatch barrier) {
		this.barrier = barrier;
	}
	
	/**
	 * Children of this class have to call this at
	 * the very end of their Runnable::run() method
	 * implementation 
	 */
	protected void triggerBarrier() {
		if(barrier != null) {
			barrier.countDown();			
		}
	}
	
	/**
	 * Do system agnostic clean up.
	 * Should be overloaded by children to do system
	 * specific clean up as well
	 */
	protected void stop() {
		messageBus.unregisterRecipient(this);
	}
	
	protected abstract void processMessages();
}
