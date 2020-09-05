package config;

public class ConfigJSON {
	
	GameServer game_server;
	DataLocations data_locations;
	
	public class GameServer {
		
		String port;
		String thread_pool_size;
		String max_tps;
		
	}
	
	public class DataLocations {
		
		String database;
		String entity_templates;
		String sprites;
		
	}
	
}
