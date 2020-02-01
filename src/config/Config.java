package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import utility.File;
import utility.Logger;

public class Config {
	
	private static final HashMap<String, String> config = new HashMap<>();
	
	public static void load(File file) {
		if(config.size() == 0) {
			try (BufferedReader br = file.getReader()){
				final StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
				}
				
				final Gson gson = new Gson();
				final ConfigJSON configJson = gson.fromJson(sb.toString(), ConfigJSON.class);
				// Game server configs
				config.put("port", configJson.game_server.port);
				config.put("thread_pool_size", configJson.game_server.thread_pool_size);
				config.put("max_tps", configJson.game_server.max_tps);
				// Data location configs
				config.put("database_locations", configJson.data_locations.database);
				config.put("entity_templates", configJson.data_locations.entity_templates);
				config.put("sprite_locations", configJson.data_locations.sprites);
				
			} catch (IOException ex) {
				Logger.ERROR.log("Failed to read config.json");
			}
			
		}
	}
	
	public static String get(String key) {
		return config.get(key);
	}
}
