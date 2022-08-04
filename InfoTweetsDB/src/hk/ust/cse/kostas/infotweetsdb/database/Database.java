/*
 * Created on February 20, 2015 by kostas-κγ  
 *
 * This is part of the InfoTweetsDB project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package hk.ust.cse.kostas.infotweetsdb.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import hk.ust.cse.kostas.infotweetsdb.util.ProjectProperties;  

//import datastructures.tuple.JoinTuple;
//import datastructures.tuple.SingleTuple;
//import datastructures.tweet.ConversationList;
//import datastructures.Message;

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
    private final String DATABASE;
    private final String TABLE;
    private String URL;
    private final String USER = "kostas";
    private final String PASSWORD = "password";
    
    private Connection connection;

    public Database(ProjectProperties properties ) {
	this.DATABASE = properties.database;
	this.TABLE = properties.table;
	this.URL = "jdbc:" + this.DBMS + "://" + this.HOST + ":" 
	    + this.PORT + "/" + this.DATABASE;
	System.out.println(this.URL);
	registerJDBCDriver();
	this.connection = establishConnectionDB();
    }


    public void insertTuple(Tuple tuple) {
	String sql = "INSERT INTO " + this.TABLE +" VALUES (" 
	    + tuple.toSQLInsertionString() + ")";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    statement.executeUpdate(sql);
	} catch(SQLException sqlex) { /*
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
    } // insertTuple()

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

    public List<Tuple> extractTuples() {
	List<Tuple> listofTuples = new ArrayList<Tuple>();
	String query = "select * from " + this.TABLE + ";";
	Statement statement = null;
	try {
	    statement = this.connection.createStatement();
	    ResultSet resultset = statement.executeQuery(query);
	    while(resultset.next()) {
		Tuple tuple = new Tuple();
		tuple.messageID = resultset.getBigDecimal("message_id");
		tuple.authorID = resultset.getBigDecimal("author_id");
		tuple.message = resultset.getString("message");
		tuple.informative = resultset.getBoolean("informative");
		tuple.category = resultset.getInt("category");
		tuple.isReply = resultset.getBoolean("is_reply");
		tuple.replyToMessageID = 
		    resultset.getBigDecimal("reply_to_message_id");
		tuple.replyToAuthorID = 
		    resultset.getBigDecimal("reply_to_author_id");
		tuple.isRetweet = resultset.getBoolean("is_retweet");
		tuple.retweetFromMessageID = 
		    resultset.getBigDecimal("retweet_from_message_id");
		tuple.retweetFromAuthorID = 
		    resultset.getBigDecimal("retweet_from_author_id");

		listofTuples.add(tuple);
	    }	
	} catch(SQLException e) {
	    e.printStackTrace();
	}
	return listofTuples;
    }
    
    private void registerJDBCDriver() {
	System.out.println("------ MySQL JDBC Connection Testing ------");
	try {
	    Class.forName(this.DRIVER);
	} catch(ClassNotFoundException e) {
	    System.out.println("MySQL JDBC Driver not found.");
	    e.printStackTrace();
	    System.exit(1);
	}
	System.out.println("MySQL JDBC Driver Registered.");
    }

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