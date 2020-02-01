package main;

import bus.MessageBus;
import utility.Logger;

public class DummySystem extends AbstractSystem {

	public DummySystem(MessageBus messageBus) {
		super(messageBus);
	}

	@Override
	protected void processMessages() {
		
	}
	
	@Override
	public void run() {
		Logger.INFO.log("System " + super.toString() + " is running");
		processMessages();
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			Logger.ERROR.log("Dummy was interrupted while taking a nap");
			e.printStackTrace(Logger.ERROR.getPrintStream());
		}
		super.triggerBarrier();
	}
	
}
