package config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.gson.Gson;

import utility.Logger;

public class Config {
	
	private static final HashMap<String, String> config = new HashMap<>();
	
	public static void load(String path) {
		if(config.size() == 0) {
			Logger.INFO.log("Loading config.json");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(path)))){
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
				System.exit(1);
			}
		}
	}
	
	public static String getAsString(String key) {
		if(!config.containsKey(key)) {
			Logger.ERROR.log("An unknown config value was requested");
			return "";
		} else if (config.size() == 0) {
			Logger.ERROR.log("Something tried to access configs before the config was loaded");
			throw new IllegalStateException("Config file has not been loaded yet");
		}
		return config.get(key);
	}
	
	public static int getAsInt(String key) {
		if(!config.containsKey(key)) {
			Logger.ERROR.log("An unknown config value was requested");
			return 0;
		} else if (config.size() == 0) {
			Logger.ERROR.log("Something tried to access configs before the config was loaded");
			throw new IllegalStateException("Config file has not been loaded yet");
		}
		return Integer.valueOf(config.get(key));
	}
	
}
