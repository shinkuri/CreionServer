package nomm;

import java.sql.SQLException;

import databases.ECDatabase;
import databases.PositionDatabase;
import databases.StatusDatabase;

public class Main {
	
	public static void main(String[] args) {
		
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
		}
	}
}
