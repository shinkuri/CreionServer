package main;

import java.sql.SQLException;

import bus.MessageBus;
import config.Config;
import databases.ECDatabase;
import utility.Logger;

public class EntitySystem extends AbstractSystem {

	private ECDatabase database;
	
	public EntitySystem(MessageBus messageBus) {
		super(messageBus);
		
		try {
			database = new ECDatabase("jdbc:sqlite:" + Config.getAsString("database_locations") + "entity_database.db");
		} catch (SQLException e) {
			Logger.ERROR.log("Failed to start the Entity Database connection");
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processMessages() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void stop() {
		super.stop();
		database.close();
	}

}
