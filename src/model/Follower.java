package model;

import java.util.ArrayList;
import java.util.Date;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Follower Model for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class Follower {

	/** List of Attributes */
	private String username;
	private String follower;
	private Date since;

	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS followers ("
			+ "username text,"
			+ "follower text,"
			+ "since timestamp,"
			+ "PRIMARY KEY (username, follower)" + ")";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
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
		
		ps = session.prepare("INSERT INTO followers (username, follower, since) VALUES (?, ?, ?)");
		session.execute(ps.bind(username, follower, since));
		ret = true;
		
		return ret;
	}
	
	public boolean addTweet(Session session) {
		return false;
	}
	
	public static ArrayList<String> retrieveFollowers(Session session, String username) {
		ArrayList<String> ret = new ArrayList<String>();
		PreparedStatement ps;
		
		ps = session.prepare("SELECT * FROM followers WHERE username= ? ");
		ResultSet res = session.execute(ps.bind(username));
		
		for (Row r : res) {
			ret.add(r.getString("follower"));
		}
		
		return ret;
	}

}
