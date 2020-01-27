package bus;

import main.AbstractSystem;

public class Message {
	
	private final AbstractSystem recipient;
	private final int behaviorID;
	private final Object[] args;

	private Object result = null;
	private boolean complete = false;
	
	public Message(AbstractSystem recipient, int behaviorID, Object... args) {
		this.recipient = recipient;
		this.behaviorID = behaviorID;
		this.args = args;
	}
	
	public AbstractSystem getRecipient() {
		return recipient;
	}
	
	public int getBehaviorID() {
		return behaviorID;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setResult(Object o) {
		result = o;
	}
	
	public Object getResult() {
		return result;
	}
	
	public boolean hasResult() {
		return (result == null) ? false : true;
	}
	
	public void setComplete() {
		complete = true;
	}
	public boolean isComplete() {
		return complete;
	}
	
}
