package model;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * User Model for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class User {

	/** List of Attributes */
	private String username;
	private String password;

	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users ("
			+ "username text PRIMARY KEY, " + "password text" + ")";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	// For security, don't provide getter for password

	public void setPassword(String password) {
		this.password = password;
	}

	public void save(Session session) {
		PreparedStatement ps = session.prepare("INSERT INTO users (username, password) VALUES (?, ?)");
		session.execute(ps.bind(username, password));
	}
	
	public boolean login(Session session) {
		boolean ret = false;
		PreparedStatement ps = session.prepare("SELECT password FROM users WHERE username= ? ");
		
		ResultSet result = session.execute(ps.bind(username));
		for (Row row : result) {
			if (row.getString("password").equals(password)) ret = true;
		}
		
		return ret;
	}

}
