package databases;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import utility.Logger;

public class ECDatabase implements Closeable {
	
	private static final HashMap<String, String> queries = new HashMap<>();
	static {
		final String end = ");";
		StringBuilder s = new StringBuilder();
		s.append("CREATE TABLE IF NOT EXISTS templates(");
		s.append("template_id INT PRIMARY KEY,");
		s.append("human_readable_name CHAR");
		s.append(end);
		queries.put("create_templates", s.toString());
		s = new StringBuilder();
		
		s.append("CREATE TABLE IF NOT EXISTS template_component_mapping(");
		s.append("template_id INT,");
		s.append("component_id INT");
		s.append(end);
		queries.put("create_template_component_mapping", s.toString());
		s = new StringBuilder();
		
		s.append("CREATE TABLE IF NOT EXISTS entities(");
		s.append("entity_id INT PRIMARY KEY,");
		s.append("human_readable_name CHAR");
		s.append(end);
		queries.put("create_entities", s.toString());
		s = new StringBuilder();
		
		s.append("CREATE TABLE IF NOT EXISTS components(");
		s.append("component_id INT PRIMARY KEY,");
		s.append("human_readable_name CHAR,");
		s.append("table_name CHAR");
		s.append(end);
		queries.put("create_components", s.toString());
		s = new StringBuilder();
		
		s.append("CREATE TABLE IF NOT EXISTS entity_component_mapping(");
		s.append("entity_id INT,");
		s.append("component_id INT,");
		s.append("component_data_id INT");
		s.append(end);
		queries.put("create_entity_component_mapping", s.toString());
		s = new StringBuilder();
		
	}
	
	private final Connection dbConn; 
	
	public ECDatabase(String dbPath) throws SQLException {
		
		dbConn = DriverManager.getConnection(dbPath);
		Logger.INFO.log("Connected to Entity Database");

		final Statement stmt = dbConn.createStatement();
		stmt.addBatch(queries.get("create_templates"));
		stmt.addBatch(queries.get("create_template_component_mapping"));
		stmt.addBatch(queries.get("create_entities"));
		stmt.addBatch(queries.get("create_components"));
		stmt.addBatch(queries.get("create_entity_component_mapping"));
		stmt.executeBatch();
		
		Logger.INFO.log("Executed Entity Database DDL");
		
	}
	
	@Override
	public void close() {
		try {
			dbConn.close();
		} catch(SQLException e) {
			Logger.ERROR.log("Exception while closing Entity Database connection");
		}
	}
	
	
}
