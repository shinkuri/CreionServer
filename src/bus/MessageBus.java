package bus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.AbstractSystem;

public class MessageBus {
			
	private final HashMap<AbstractSystem, HashSet<Message>> queues = new HashMap<>();
	private final HashMap<AbstractSystem, HashSet<Message>> queuesBuffer = new HashMap<>();
	
	public synchronized void registerRecipient(AbstractSystem recipient) {
		if(!queues.containsKey(recipient)) {
			queues.put(recipient, new HashSet<>());
			queuesBuffer.put(recipient, new HashSet<>());
		}
	}
	
	public synchronized void unregisterRecipient(AbstractSystem recipient) {
		queues.remove(recipient);
		queuesBuffer.remove(recipient);
	}
	
	/**
	 * Synchronized method to retrieve all pending messages for one listener.
	 * 
	 * @return Set of pending messages for this listener, or an empty Set if the listener is unknown.
	 */
	public Set<Message> receive(AbstractSystem listener) {
		synchronized(queues) {
			return (queues.containsKey(listener)) ? queues.get(listener) : new HashSet<>();
		}
	}
	
	/** New generic message function to reduce API clutter.
	 * @param recipient = a registered recipient system.
	 * @param behaviourID = a specific behavior (function) to be applied to some data. Resolved inside the recipient system.
	 * @param args = data to be passed along to the behavior function that is mapped to behaviorID.
	 * @return
	 */
	public Message send(AbstractSystem recipient, int behaviorID, Object... args) {
		synchronized(queuesBuffer) {
			if(queuesBuffer.containsKey(recipient)) {
				final Message message = new Message(recipient, behaviorID, args);
				queuesBuffer.get(recipient).add(message);
				return message;
			}
			return null;
		}
	}
	
	/**
	 * Call once after the current sender has sent all messages it plans on sending for the moment.
	 * Without at least one call, no messages will be available for any recipient.
	 * Do not call after every sent message.
	 */
	public synchronized void commit() {
		for(AbstractSystem recipient : queues.keySet()) {
			final HashSet<Message> t = queues.get(recipient);
			t.addAll(queuesBuffer.get(recipient));
			queuesBuffer.put(recipient, new HashSet<>());
		}
	}
	
}
