package main;

import bus.MessageBus;
import utility.Logger;

public class DummySystem extends AbstractSystem {
	
	private final int delayMS;
	
	public DummySystem(MessageBus messageBus, int delayMS) {
		super(messageBus);
		
		this.delayMS = delayMS;
	}

	@Override
	protected void processMessages() {
		
	}
	
	@Override
	public void run() {
		Logger.INFO.log("System " + super.toString() + " is running");
		processMessages();
		try {
			Thread.sleep(delayMS);
		} catch (InterruptedException e) {
			Logger.ERROR.log("Dummy was interrupted while taking a nap");
			e.printStackTrace(Logger.ERROR.getPrintStream());
		}
		super.triggerBarrier();
	}
	
}
