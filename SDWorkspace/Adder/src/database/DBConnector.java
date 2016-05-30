package database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.*;
public class DBConnector {
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String username = "root";
	private static final String password = "aris";
	private static final String myurl = "jdbc:mysql://localhost/Database";

	static { // loading the database driver
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}
	

	static java.sql.Connection getDBConnection() {

		java.sql.Connection dbConnection = null;
		
		try {
			 
			dbConnection = DriverManager.getConnection(myurl,username,password);
			return dbConnection;
 
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}
		return dbConnection;	
	
	}
	
}
