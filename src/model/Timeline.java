package model;

import java.util.ArrayList;
import java.util.UUID;

import com.datastax.driver.core.Session;

/**
 * Timeline Model for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class Timeline {

	/** List of Attributes */
	private String username;
	private UUID time;
	private UUID tweet_id;

	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS timeline ("
			+ "username text," + "time timeuuid," + "tweet_id uuid,"
			+ "PRIMARY KEY (username, time)"
			+ ") WITH CLUSTERING ORDER BY (time DESC)";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getTime() {
		return time;
	}

	public void setTime(UUID time) {
		this.time = time;
	}

	public UUID getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(UUID tweet_id) {
		this.tweet_id = tweet_id;
	}
	
	public boolean addTweet(Session session) {
		return false;
	}
	
	public ArrayList<Tweet> retrieveTimeline(Session session) {
		ArrayList<Tweet> ret = null;
		return ret;
	}

}
