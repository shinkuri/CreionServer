package main;

import bus.MessageBus;
import databases.PositionDatabase;

public class PositionSystem extends AbstractSystem {
	
	private PositionDatabase database;
	
	public PositionSystem(MessageBus messageBus) {
		super(messageBus);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processMessages() {
		// TODO Auto-generated method stub
		
	}

}
