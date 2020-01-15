package nomm;

import bus.MessageBus;
import network.NetworkSystem;

public class Main {
	
	public static void main(String[] args) {
		
		final int port = Integer.parseInt(args[0]);
		
		final MessageBus messageBus = new MessageBus();
		
		final NetworkSystem networkSystem = new NetworkSystem(messageBus, port);
		
		
		networkSystem.stop();
		/*
		try (ECDatabase ecDatabase = new ECDatabase("jdbc:sqlite:data/db.db")) {
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try (StatusDatabase statusDatabase = new StatusDatabase("jdbc:sqlite:data/status_db.db")) {
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try (PositionDatabase positionDatabase = new PositionDatabase("jdbc:sqlite:data/position_db.db")) {
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
}
