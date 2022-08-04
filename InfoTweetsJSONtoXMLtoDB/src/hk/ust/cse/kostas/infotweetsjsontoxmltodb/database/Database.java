/*
 * Created on March 2, 2015 by kostas-κγ  
 *
 * This is part of the InfoTweetsJSONtoXMLtoDB project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetsjsontoxmltodb.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import infotweetsjsontoxmltodb.entities.Tweet;
import infotweetsjsontoxmltodb.entities.TweetLabel;
import infotweetsjsontoxmltodb.entities.User;

/**
 *
 *
 * @author kostas-κγ   
 */
public final class Database {

    private final String DRIVER = "org.postgresql.Driver";
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DBMS = "postgresql";
    private final String DATABASE = "twitterall";
    private final String TWEET_TABLE = "tweets";
    private final String USER_TABLE = "users";
    private final String RETWEET_TABLE = "retweets";
    private final String REPLY_TABLE = "replies";
    private final String MESSAGE_USERS_TABLE = "messageusers";
    private final String TWEET_LABEL_TABLE = "tweetslabels";
    private String URL;
    private final String USER = "kostas";
    private final String PASSWORD = "password";
    
    private Connection connection;

    public Database() {
	this.URL = "jdbc:" + this.DBMS + "://" + this.HOST + ":" 
	    + this.PORT + "/" + this.DATABASE;
	System.out.println(this.URL);
	registerJDBCDriver();
	this.connection = establishConnectionDB();
    }

    public void insertTweetTuple(Tweet tweet) {
	String sql = "INSERT INTO " + this.TWEET_TABLE +" VALUES (" 
	    + tweet.toSQLInsertionString() + ")";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { 
	    sqlex.printStackTrace();
/*
	        this.logfiles.appendPopulateDatabaseErrorLog
		    (article.getId() + "\n");
		StringBuilder sb = new StringBuilder();
		sb.append(sqlex + "\n");
		sb.append(sqlex.getMessage() + "\n");
		sb.append(sqlex.getCause() + "\n");
		StackTraceElement[] st = sqlex.getStackTrace();
		for(StackTraceElement s : st) {
		    sb.append(s);
		    sb.append("\n");
		}
		sb.append("\n");
		this.logfiles.appendPopulateDatabaseErrorLog(sb.toString());
		this.logfiles.flushPopulateDatabaseErrorLog();
		System.out.println("Article "+ article.getId() 
				   +" was not inserted correctly.");
				   sqlex.printStackTrace();*/
	}
    } // insertTweetTuple()

    public void insertUserTuple(User user) {
	String sql = "INSERT INTO " + this.USER_TABLE +" VALUES (" 
	    + user.toSQLInsertionString() + ")";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { 
	    sqlex.printStackTrace();
	    /*				  
	        this.logfiles.appendPopulateDatabaseErrorLog
		    (article.getId() + "\n");
		StringBuilder sb = new StringBuilder();
		sb.append(sqlex + "\n");
		sb.append(sqlex.getMessage() + "\n");
		sb.append(sqlex.getCause() + "\n");
		StackTraceElement[] st = sqlex.getStackTrace();
		for(StackTraceElement s : st) {
		    sb.append(s);
		    sb.append("\n");
		}
		sb.append("\n");
		this.logfiles.appendPopulateDatabaseErrorLog(sb.toString());
		this.logfiles.flushPopulateDatabaseErrorLog();
		System.out.println("Article "+ article.getId() 
				   +" was not inserted correctly.");
				   sqlex.printStackTrace();*/
	}
    } // insertUserTuple()

    public void insertRetweetTuple(BigDecimal retweetID, 
				   BigDecimal originalID) {
	String sql = "INSERT INTO " + this.RETWEET_TABLE +" VALUES (" 
	    + retweetID + "," + originalID + ")";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { 
	    sqlex.printStackTrace();
	}
    } // insertRetweetTuple()

    public void insertReplyTuple(BigDecimal replyID, 
				 BigDecimal originalID, 
				 BigDecimal originalUserID, 
				 String originalScreenName) {
	String sql = "INSERT INTO " + this.REPLY_TABLE +" VALUES (" 
	    + replyID + "," + originalID + "," + originalUserID + ",'" 
	    + originalScreenName + "')";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { 
	    sqlex.printStackTrace();
	}
    } // insertReplyTuple()

    public void insertMessageUsersTuple(BigDecimal messageID, 
					BigDecimal userID, String userName,
					String userScreenName) {

	String sql = "INSERT INTO "+this.MESSAGE_USERS_TABLE +" VALUES (" 
	    + messageID + "," + userID + ",'" + userName + "','" 
	    + userScreenName + "')";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { 
	    sqlex.printStackTrace();
	}
    } // insertMessageUsersTuple()

    public void insertTweetLabelTuple(TweetLabel tweetlabel) {
	String sql = "INSERT INTO " + this.TWEET_LABEL_TABLE +" VALUES (" 
	    + tweetlabel.toSQLInsertionString() + ")";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { 
	    sqlex.printStackTrace();
	}
    }

    /*
    public List<JoinTuple> extractJoinTuples() {
	List<JoinTuple> listofTuples = new ArrayList<JoinTuple>();
	String query = Query.joinReplyToPostID;
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    ResultSet resultset = statement.executeQuery(query);
	    while(resultset.next()) {
		BigDecimal id1 = resultset.getBigDecimal("t1id");
		BigDecimal id2 = resultset.getBigDecimal("t2id");
		Message content1 = 
		    new Message(resultset.getString("t1content"));
		Message content2 = 
		    new Message(resultset.getString("t2content"));
		JoinTuple tuple = new JoinTuple(id1,id2,content1,content2);
		listofTuples.add(tuple);
	    }	
	} catch(SQLException e) {
	    e.printStackTrace();
	}
	return listofTuples;
	}*/

    /*
     * Gets all tuples from relation labels.
     * This is a relation taken from another database.
     * It was not initially created by this project.
     *
    public List<LabelTuple> extractTuples() {
	List<LabelTuple> listofTuples = new ArrayList<LabelTuple>();
	String query = "select * from " + "labels" + ";";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    ResultSet resultset = statement.executeQuery(query);
	    while(resultset.next()) {
		LabelTuple tuple = new LabelTuple();
		tuple.message = resultset.getString("message");
		tuple.informative = resultset.getBoolean("informative");
		tuple.category = resultset.getInt("category");
		listofTuples.add(tuple);
	    }	
	} catch(SQLException e) {
	    e.printStackTrace();
	}
	return listofTuples;
    } // extractTuples()
    */
    /**
     * Gets all tuples from relation tweets;
     */
    public List<Tweet> extractTweetsTuples() {
	List<Tweet> listofTuples = new ArrayList<Tweet>();
	String query = "select * from " + this.TWEET_TABLE + ";";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    ResultSet resultset = statement.executeQuery(query);
	    while(resultset.next()) {
		Tweet tweet = new Tweet();
		tweet.id = resultset.getBigDecimal("id");
		tweet.text= resultset.getString("text");
		tweet.created_at=resultset.getTimestamp("created_at");
		tweet.retweet_count=resultset.getInt("retweet_count");
		tweet.favorite_count=resultset.getInt("favorite_count");
		listofTuples.add(tweet);
	    }	
	} catch(SQLException e) {
	    e.printStackTrace();
	}
	return listofTuples;
    } // extractTweets()

    private void registerJDBCDriver() {
	System.out.println("------ PSQL JDBC Connection Testing ------");
	try {
	    Class.forName(this.DRIVER);
	} catch(ClassNotFoundException e) {
	    System.out.println("PSQL JDBC Driver not found.");
	    e.printStackTrace();
	    System.exit(1);
	}
	System.out.println("PSQL JDBC Driver Registered.");
    } // registerJDBCDriver()

    private Connection establishConnectionDB() {
	Connection connection = null;
	try {
	    connection = DriverManager.
		getConnection(this.URL,this.USER,this.PASSWORD);
	} catch(SQLException e) {
	    System.out.println("Connection to DB failed.");
	    e.printStackTrace();
	    System.exit(1);
	}
	if(connection != null) {
	    System.out.println("Connection to database is established.");
	} else {
	    System.out.println("Failed to make connection.");
	}
	return connection;
    } // establishConnectionDB()

} // Database