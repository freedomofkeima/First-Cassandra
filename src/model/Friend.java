package model;

import java.util.Date;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Friend Model for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class Friend {

	/** List of Attributes */
	private String username;
	private String friend;
	private Date since;

	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS friends ("
			+ "username text," + "friend text," + "since timestamp,"
			+ "PRIMARY KEY (username, friend)" + ")";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}
	
	public boolean follow(Session session) {
		boolean ret = false;
		PreparedStatement ps;
		
		if (since == null)	since = new Date();
		
		ps = session.prepare("SELECT username FROM users WHERE username= ?");
		ResultSet result = session.execute(ps.bind(friend));
		for (Row row : result) {
			if (row.getString("username").equals(friend)) ret = true;
		}
		
		if (ret) {
			ps = session.prepare("INSERT INTO friends (username, friend, since) VALUES (?, ?, ?)");
			session.execute(ps.bind(username, friend, since));
			ret = true;
		}
		
		if (ret) {
			Follower add_follower = new Follower();
			add_follower.setUsername(friend);
			add_follower.setFollower(username);
			add_follower.setSince(since); // use same timestamp
			ret = add_follower.follow(session);
		}
		
		return ret;
	}

}
