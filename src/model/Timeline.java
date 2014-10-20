package model;

import java.util.ArrayList;
import java.util.UUID;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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
	
	public void addTweet(Session session) {
		PreparedStatement ps = session.prepare("INSERT INTO timeline (username, time, tweet_id) VALUES (?, ?, ?)");
		session.execute(ps.bind(username, time, tweet_id));
	}
	
	public ArrayList<Tweet> retrieveTimeline(Session session) {
		ArrayList<Tweet> ret = new ArrayList<Tweet>();

		PreparedStatement ps = session.prepare("SELECT * FROM timeline WHERE username= ?");
		ResultSet resultset = session.execute(ps.bind(username));
		
		// TODO : We need to change the database structure!
		for (Row r : resultset) {
			Tweet n_tweet = new Tweet();
			n_tweet.setTweet_id(r.getUUID("tweet_id"));
			n_tweet.retrieveTweet(session);
			ret.add(n_tweet);
		}
		
		return ret;
	}

}
