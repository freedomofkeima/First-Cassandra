package model;

import java.util.ArrayList;
import java.util.UUID;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

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
	
	public void retrieveTweet(Session session) {
		PreparedStatement ps = session.prepare("SELECT * FROM tweets WHERE tweet_id = ? ");
		ResultSet res = session.execute(ps.bind(tweet_id));
		for (Row r : res) {
			username = r.getString("username");
			body = r.getString("body");
		}
	}
	
	public boolean tweeting(Session session) {
		boolean ret = false;
		tweet_id = UUIDs.random();
		UUID time = UUIDs.timeBased();
		
		// Insert to Tweet
		PreparedStatement ps = session.prepare("INSERT INTO tweets (tweet_id, username, body) VALUES (?, ?, ?)");
		session.execute(ps.bind(tweet_id, username, body));
		
		// Insert to Userline
		Userline userline = new Userline();
		userline.setUsername(username);
		userline.setTime(time);
		userline.setTweet_id(tweet_id);
		userline.tweeting(session);
		
		// Insert to Timeline
		Timeline timeline = new Timeline();
		timeline.setUsername(username);
		timeline.setTime(time);
		timeline.setTweet_id(tweet_id);
		timeline.addTweet(session);
		
		// Retrieve All Followers
		ArrayList<String> f = Follower.retrieveFollowers(session, username);
		
		// Insert to All Followers Timeline
		for (String f_entity : f) {
			Timeline timeline_follower = new Timeline();
			timeline_follower.setUsername(f_entity);
			timeline_follower.setTime(time);
			timeline_follower.setTweet_id(tweet_id);
			timeline_follower.addTweet(session);
		}
		
		ret = true;
		
		return ret;
	}	

}
