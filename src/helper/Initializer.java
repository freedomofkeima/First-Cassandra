package helper;

import model.Follower;
import model.Friend;
import model.Timeline;
import model.Tweet;
import model.User;
import model.Userline;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

/**
 * Initializer for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class Initializer {
	
	public static Session init(Session session) {
		PreparedStatement ps;
		
		// Create keyspace if not exist
		if (Constants.IS_FIRST_RUN) {
			ps = session.prepare("CREATE KEYSPACE IF NOT EXISTS "
					+ Constants.KEYSPACE_NAME
					+ " WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1} ");
			session.execute(ps.bind());
		}

		// Connect to keyspace
		ps = session.prepare("USE " + Constants.KEYSPACE_NAME);
		session.execute(ps.bind());

		if (Constants.IS_FIRST_RUN) {
			// Create (IF NOT EXIST) -> can be refactored later
			ps = session.prepare(Follower.CREATE_TABLE);
			session.execute(ps.bind());
			ps = session.prepare(Friend.CREATE_TABLE);
			session.execute(ps.bind());
			ps = session.prepare(Timeline.CREATE_TABLE);
			session.execute(ps.bind());
			ps = session.prepare(Tweet.CREATE_TABLE);
			session.execute(ps.bind());
			ps = session.prepare(User.CREATE_TABLE);
			session.execute(ps.bind());
			ps = session.prepare(Userline.CREATE_TABLE);
			session.execute(ps.bind());
		}
		
		return session;
	}

}
