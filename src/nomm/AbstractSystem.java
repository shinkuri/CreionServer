package nomm;

import java.util.UUID;

import bus.MessageBus;

public abstract class AbstractSystem implements Runnable {
	
	private final UUID uuid = UUID.randomUUID();
	
	protected final MessageBus messageBus;
	
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
	
	/**
	 * Do system agnostic clean up.
	 */
	protected void stop() {
		messageBus.unregisterRecipient(this);
	}
	
	protected abstract void processMessages();
}
