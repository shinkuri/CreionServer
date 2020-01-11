package databases;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import utility.Logger;

public class PositionDatabase implements Closeable {
	
	private final Connection dbConn;
	
	public PositionDatabase(String dbPath) throws SQLException {
		
		dbConn = DriverManager.getConnection(dbPath);
		Logger.INFO.log("Connected to Position Database");
		// DDL
		final String DDLdata = "CREATE TABLE IF NOT EXISTS data_position(\n"
				+ "component_data_id INT PRIMARY KEY,\n"
				+ "x_position FLOATT,\n"
				+ "y_position FLOAT);";
		final String DDLtemplates = "CREATE TABLE IF NOT EXISTS position_data_templates(\n"
				+ "template_id INT\n"
				+ "x_position FLOAT,\n"
				+ "y_position FLOAT);";
		
		final Statement stmt = dbConn.createStatement();
		stmt.addBatch(DDLdata);
		stmt.addBatch(DDLtemplates);
		stmt.executeBatch();
		
		Logger.INFO.log("Executed Position Database DDL");
		
		
	}
	
	@Override
	public void close() {
		try {
			dbConn.close();
		} catch(SQLException e) {
			Logger.ERROR.log("Exception while closing Position Database connection");
		}
	}
}
