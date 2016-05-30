package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class DBfunctions {

	public void insert(String node_id) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		dbConnection = DBConnector.getDBConnection();

		String insertTableSQL = "INSERT INTO users" + "(node_id) VALUES"
				+ "(?)";
		try {
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, node_id);

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("user: " + node_id
					+ " inserted into USERS table!");

		} finally {
			try {
				if (dbConnection != null) {
					dbConnection.close(); // closing connection
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if (preparedStatement != null) {
				preparedStatement.close(); // closing preparedStatement
			}
		}
	}

	@SuppressWarnings("finally")
	public boolean userExists(String username) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		dbConnection = DBConnector.getDBConnection();

		String get = "select distinct count(*) from users where node_id = ?";
		boolean check = false;

		try {
			preparedStatement = dbConnection.prepareStatement(get);
			preparedStatement.setString(1, username);

			// execute insert SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			// System.out.printf("%d",rs.getInt(1));
			if (rs.getInt(1) == 0)
				check = false;
			else
				check = true;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
			return check;
		}

	}

	@SuppressWarnings("finally")
	public String get_password(String username) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		dbConnection = DBConnector.getDBConnection();

		String get = "select * from android_users where username = ?";
		String password = null;

		try {
			preparedStatement = dbConnection.prepareStatement(get);
			preparedStatement.setString(1, username);

			// execute insert SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				password = rs.getString(2);
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
			return password;
		}

	}

	public void insert_android_user(String username, String password) {

		Connection dbConnection = null;
		PreparedStatement ps = null;
		dbConnection = DBConnector.getDBConnection();

		String submitQuery = "INSERT INTO android_users"
				+ "( username, password ) VALUES"
				+ "(?,?)";

		try {
			ps = dbConnection.prepareStatement(submitQuery);
			ps.setString(1, username);
			ps.setString(2, password);

			ps.executeUpdate();

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("Insert user_ips error.");
		} finally {
			try {
				if (dbConnection != null) {
					dbConnection.close(); // closing connection
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if (ps != null) {
				try { // closing ps
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void insert_device(String username, String node_id ){

		Connection dbConnection = null;
		PreparedStatement ps = null;
		dbConnection = DBConnector.getDBConnection();

		String submitQuery = "INSERT INTO androidUser_has_devices"
				+ "( android_username, device ) VALUES"
				+ "(?,?)";

		try {
			ps = dbConnection.prepareStatement(submitQuery);
			ps.setString(1, username);
			ps.setString(2, node_id);

			ps.executeUpdate();

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("Insert user_ips error.");
		} finally {
			try {
				if (dbConnection != null) {
					dbConnection.close(); // closing connection
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if (ps != null) {
				try { // closing ps
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* INSERT IP!!! */
	public void insertIPStatistics(String node_id, String interface_name,
			String interface_ip, String mal_ip, int frequency) {

		Connection dbConnection = null;
		PreparedStatement ps = null;
		dbConnection = DBConnector.getDBConnection();

		String submitQuery = "INSERT INTO user_has_ips"
				+ "( node_id, interface_name,interface_ip, malicious_ip,frequency ) VALUES"
				+ "(?,?,?,?,?)";

		try {
			ps = dbConnection.prepareStatement(submitQuery);
			ps.setString(1, node_id);
			ps.setString(2, interface_name);
			ps.setString(3, interface_ip);
			ps.setString(4, mal_ip);
			ps.setInt(5, frequency);

			ps.executeUpdate();

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("Insert user_ips error.");
		} finally {
			try {
				if (dbConnection != null) {
					dbConnection.close(); // closing connection
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if (ps != null) {
				try { // closing ps
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// INSERT PATTERNS !!!
	@SuppressWarnings("finally")
	public void insertPatternStatistics(String node_id, String interface_name,
			String interface_ip, String mal_pattern, int frequency) {

		Connection dbConnection = null;
		PreparedStatement ps = null;
		dbConnection = DBConnector.getDBConnection();

		String submitQuery = "INSERT INTO user_has_patterns"
				+ "( node_id, interface_name,interface_ip, malicious_pattern,frequency ) VALUES"
				+ "(?,?,?,?,?)";

		try {
			ps = dbConnection.prepareStatement(submitQuery);
			ps.setString(1, node_id);
			ps.setString(2, interface_name);
			ps.setString(3, interface_ip);
			ps.setString(4, mal_pattern);
			ps.setInt(5, frequency);

			ps.executeUpdate();

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("Insert user_ips error.");
		} finally {
			try {
				if (dbConnection != null) {
					dbConnection.close(); // closing connection
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if (ps != null) {
				try { // closing ps
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// RETURN IP STATISTICS
	@SuppressWarnings("finally")
	public ArrayList<String[]> getIPStatistics(String node_id)
			throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		dbConnection = DBConnector.getDBConnection();

		String get = "SELECT node_id,interface_name,interface_ip,malicious_ip,frequency FROM user_has_ips WHERE node_id = ?";

		ArrayList<String[]> list = new ArrayList<String[]>();

		try {

			preparedStatement = dbConnection.prepareStatement(get);
			preparedStatement.setString(1, node_id);
			// execute insert SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = String.valueOf(rs.getInt(5));

				list.add(row);
			}
			// return list;
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
			return list;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getPatternsStatistics(String node_id)
			throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		dbConnection = DBConnector.getDBConnection();

		String get = "SELECT node_id,interface_name,interface_ip,malicious_pattern,frequency FROM user_has_patterns WHERE node_id = ?";

		ArrayList<String[]> list = new ArrayList<String[]>();

		try {

			preparedStatement = dbConnection.prepareStatement(get);
			preparedStatement.setString(1, node_id);
			// execute insert SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = String.valueOf(rs.getInt(5));

				list.add(row);
			}
			// return list;
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
			return list;
		}
	}


		@SuppressWarnings("finally")
		public ArrayList<String> get_fullIPStatistics()
				throws SQLException {

			Connection dbConnection = null;
			PreparedStatement preparedStatement = null;
			dbConnection = DBConnector.getDBConnection();

			String get = "SELECT node_id,interface_name,interface_ip,malicious_ip,frequency FROM user_has_ips";

			ArrayList<String> list = new ArrayList<String>();

			try {

				preparedStatement = dbConnection.prepareStatement(get);

					// execute insert SQL stetement
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					String[] row = new String[5];
					row[0] = rs.getString(1);
					row[1] = rs.getString(2);
					row[2] = rs.getString(3);
					row[3] = rs.getString(4);
					row[4] = String.valueOf(rs.getInt(5));

					String record = row[0] + "#" + row[1] + "#" + row[2] + "#" + row[3] + "#" + row[4];
					list.add(record);
				}
				// return list;
			} catch (SQLException e) {

				System.out.println(e.getMessage());

			} finally {

				if (preparedStatement != null) {
					preparedStatement.close();
				}

				if (dbConnection != null) {
					dbConnection.close();
				}
				return list;
			}
		}

		@SuppressWarnings("finally")
		public ArrayList<String> get_fullPatternStatistics()
				throws SQLException {

			Connection dbConnection = null;
			PreparedStatement preparedStatement = null;
			dbConnection = DBConnector.getDBConnection();

			String get = "SELECT node_id,interface_name,interface_ip,malicious_pattern,frequency FROM user_has_patterns";

			ArrayList<String> list = new ArrayList<String>();

			try {

				preparedStatement = dbConnection.prepareStatement(get);

					// execute insert SQL stetement
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					String[] row = new String[5];
					row[0] = rs.getString(1);
					row[1] = rs.getString(2);
					row[2] = rs.getString(3);
					row[3] = rs.getString(4);
					row[4] = String.valueOf(rs.getInt(5));

					String record = row[0] + "#" + row[1] + "#" + row[2] + "#" + row[3] + "#" + row[4];
					list.add(record);
				}
				// return list;
			} catch (SQLException e) {

				System.out.println(e.getMessage());

			} finally {

				if (preparedStatement != null) {
					preparedStatement.close();
				}

				if (dbConnection != null) {
					dbConnection.close();
				}
				return list;
			}
		}

}
