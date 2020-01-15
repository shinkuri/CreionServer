package databases;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import utility.Logger;
import utility.Template;
import utility.TemplateParser;

public class ECDatabase implements Closeable {
	
	private final Connection dbConn; 
	
	public void spawnRandom() {
		final Random random = new Random();
	}
	
	public ECDatabase(String dbPath) throws SQLException {
		
		dbConn = DriverManager.getConnection(dbPath);
		Logger.INFO.log("Connected to Entity Database");
		// DDL
		final String DDLtemplates = "CREATE TABLE IF NOT EXISTS templates(\n"
				+ "template_id INT PRIMARY KEY,\n"
				+ "human_readable_name CHAR);";
		final String DDLtemplateComponentMapping = "CREATE TABLE IF NOT EXISTS \n"
				+ "template_component_mapping(\n"
				+ "template_id INT, component_id INT);";
		final String DDLentities = "CREATE TABLE IF NOT EXISTS entities(\n"
				+ "entity_id INT PRIMARY KEY,\n"
				+ "human_readable_name CHAR);";
		final String DDLcomponents = "CREATE TABLE IF NOT EXISTS components(\n"
				+ "component_id INT PRIMARY KEY,\n"
				+ "human_readable_name CHAR,\n"
				+ "table_name CHAR);";
		final String DDLentityComponentMapping = "CREATE TABLE IF NOT EXISTS \n"
				+ "entity_component_mapping(\n"
				+ "entity_id INT, component_id INT, component_data_id INT);";
		
		final Statement stmt = dbConn.createStatement();
		stmt.addBatch(DDLtemplates);
		stmt.addBatch(DDLtemplateComponentMapping);
		stmt.addBatch(DDLentities);
		stmt.addBatch(DDLcomponents);
		stmt.addBatch(DDLentityComponentMapping);
		stmt.executeBatch();
		
		Logger.INFO.log("Executed Entity Database DDL");
		
		for(Template template : TemplateParser.parse("/nomm/templates.txt")) {
			// already exists?
			
			// if no, do thing
		}
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
