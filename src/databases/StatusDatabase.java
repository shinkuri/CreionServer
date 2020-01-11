package databases;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import utility.Logger;

public class StatusDatabase implements Closeable {

	private final Connection dbConn;
	
	public StatusDatabase(String dbPath) throws SQLException {
		
		dbConn = DriverManager.getConnection(dbPath);
		Logger.INFO.log("Connected to Status Database");
		// DDL
		final String DDLdata = "CREATE TABLE IF NOT EXISTS data_status(\n"
				+ "component_data_id INT PRIMARY KEY,\n"
				+ "level INT,\n"
				+ "exp INT,\n"
				+ "health INT,\n"
				+ "vitality INT,\n"
				+ "intelligence INT);";
		final String DDLtemplates = "CREATE TABLE IF NOT EXISTS status_data_templates(\n"
				+ "template_id INT\n"
				+ "level INT,\n"
				+ "exp INT,\n"
				+ "health INT,\n"
				+ "vitality INT,\n"
				+ "intelligence INT);";
		
		final Statement stmt = dbConn.createStatement();
		stmt.addBatch(DDLdata);
		stmt.addBatch(DDLtemplates);
		stmt.executeBatch();
		
		Logger.INFO.log("Executed Status Database DDL");
		
		
	}
	
	@Override
	public void close() {
		try {
			dbConn.close();
		} catch(SQLException e) {
			Logger.ERROR.log("Exception while closing Status Database connection");
		}
	}
}
