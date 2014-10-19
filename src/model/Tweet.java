package model;

import java.util.UUID;

import com.datastax.driver.core.Session;

/**
 * Tweet Model for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class Tweet {

	/** List of Attributes */
	private UUID tweet_id;
	private String username;
	private String body;

	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS tweets ("
			+ "tweet_id uuid PRIMARY KEY," + "username text," + "body text"
			+ ")";

	public UUID getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(UUID tweet_id) {
		this.tweet_id = tweet_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public boolean tweeting(Session session) {
		
		// Insert to Userline
		
		// Insert to Timeline
		
		// Retrieve All Followers
		
		// Insert to All Followers Timeline
		
		return false;
	}	

}
