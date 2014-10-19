package core;

import helper.Constants;
import helper.Initializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Friend;
import model.Timeline;
import model.Tweet;
import model.User;
import model.Userline;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Main class for Cassandra First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since September 17, 2014
 *
 */

public class Main {

	public static void main(String[] args) throws Exception {
		Cluster cluster;
		Session session;
		String input;
		User user = new User();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		// Connect to the cluster and keyspace "demo"
		cluster = Cluster.builder().addContactPoint(Constants.SERVER_ADDRESS)
				.build();
		session = cluster.connect();

		System.out.println("-- Initializing --");
		session = Initializer.init(session);
		System.out.println("-- Finished Initializing --");

		/** Home */
		boolean isFinished = false;
		boolean isLoggedIn = false;
		while (!isFinished) {
			if (!isLoggedIn) {
				String username, password;

				System.out.println("-- Main Menu -- ");
				System.out.println("1. Sign In");
				System.out.println("2. Sign Up");
				System.out.println("999. Exit");
				System.out.print("Input: ");
				System.out.flush();

				input = reader.readLine();

				switch (input) {
				case "1":
					System.out.print("Username: ");
					System.out.flush();
					username = reader.readLine();
					System.out.print("Password: ");
					System.out.flush();
					password = reader.readLine();

					user.setUsername(username);
					user.setPassword(password);
					isLoggedIn = user.login(session);

					if (isLoggedIn)
						System.out.println("\nWelcome, " + username + " !");
					else
						System.out
								.println("\nUsername / Password does not exist!");

					break;
				case "2":
					System.out.print("Username: ");
					System.out.flush();
					username = reader.readLine();
					System.out.print("Password: ");
					System.out.flush();
					password = reader.readLine();

					// TODO : Prior checking for username uniqueness, Hash
					// password
					user.setUsername(username);
					user.setPassword(password);
					user.save(session);

					isLoggedIn = true;
					break;
				case "999":
					isFinished = true;
					break;
				default:
					System.out.println("Unrecognized Input!");
				}
			} else {
				String friend_username, user_tweet, username;
				boolean is_updated;
				ArrayList<Tweet> tweets;

				System.out.println("-- Menu -- ");
				System.out.println("1. Follow a Friend");
				System.out.println("2. Tweet");
				System.out.println("3. Show Userline");
				System.out.println("4. Show Timeline");
				System.out.println("888. Sign Out");
				System.out.println("999. Exit");
				System.out.print("Input: ");
				System.out.flush();

				input = reader.readLine();

				switch (input) {
				case "1":
					System.out.print("Friend's Username: ");
					System.out.flush();
					friend_username = reader.readLine();
					
					Friend add_friend = new Friend();
					add_friend.setUsername(user.getUsername());
					add_friend.setFriend(friend_username);
					
					is_updated = add_friend.follow(session);
					
					if (is_updated) {
						System.out.println("You have followed " + friend_username + " !");
					} else {
						System.out.println("Fail to follow " + friend_username +" !");
					}
					break;
				case "2":
					System.out.print("Your Tweet: ");
					System.out.flush();
					user_tweet = reader.readLine();
					
					Tweet add_tweet = new Tweet();
					add_tweet.setUsername(user.getUsername());
					add_tweet.setBody(user_tweet);
					
					is_updated = add_tweet.tweeting(session);
					
					if (is_updated) {
						System.out.println("Your new tweet has been posted successfully!");
					} else {
						System.out.println("Error in tweeting!");
					}
					
					
					break;
				case "3":
					System.out.print("Choose username for userline: ");
					System.out.flush();
					username = reader.readLine();
					
					Userline userline = new Userline();
					userline.setUsername(username);
					
					tweets = userline.retrieveUserline(session);
					showTweets(tweets);
					break;
				case "4":
					System.out.print("Choose username for timeline: ");
					System.out.flush();
					username = reader.readLine();
					
					Timeline timeline = new Timeline();
					timeline.setUsername(username);
					
					tweets = timeline.retrieveTimeline(session);
					showTweets(tweets);
					break;
				case "888":
					isLoggedIn = false;
					System.out.println("You have been logged out successfully!");
					break;
				case "999":
					isFinished = true;
					break;
				default:
					System.out.println("Unrecognized Input!");
				}

				isFinished = true;
			}
		}

		// Clean up the connection by closing it
		cluster.close();
	}
	
	private static void showTweets(ArrayList<Tweet> tweets) {
		if (tweets.size() == 0) System.out.println("- No Tweet -");
		else {
			System.out.println("- Tweet(s) -");
			for (Tweet t : tweets) {
				System.out.println(t.getUsername() + " : " + t.getBody());
			}
		}
	}

}
