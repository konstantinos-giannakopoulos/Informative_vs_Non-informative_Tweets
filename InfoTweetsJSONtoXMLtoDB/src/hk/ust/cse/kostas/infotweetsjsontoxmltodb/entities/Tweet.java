package infotweetsjsontoxmltodb.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Tweet {
    public TweetStatus status;
    public BigDecimal id;    
    public BigDecimal userid;
    public String text;
    public Timestamp created_at;
    public int retweet_count;
    public int favorite_count;
    public boolean informative;
    public int category;

    public Tweet(TweetStatus status, 
		 BigDecimal id, BigDecimal userid, String text, 
		 Timestamp created_at, int retweet_count, 
		 int favorite_count, boolean informative, int category) {
	this.status = status;
	this.id = id;	
	this.userid = userid;
	this.text = text;
	this.created_at = created_at;
	this.retweet_count = retweet_count;
	this.favorite_count = favorite_count;
	this.informative = informative;
	this.category = category;
    }

    public Tweet() {

    }

    public String toSQLInsertionString() {
	String sqlInsert = this.id + "," + this.userid + ",'" + this.text 
	    + "','" + this.created_at + "'," + this.retweet_count + "," 
	    + this.favorite_count + ",'" 
	    + this.informative + "'," + this.category 
	    ; 
	return sqlInsert;
    } // toSQLInsertionString()

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(" - Tweet Details - ");
	sb.append("\n[status]: " + this.status);
	sb.append("\n[id]: " + this.id);
	sb.append("\n[Userid]: " + this.userid);
	sb.append("\n[Text]: " + this.text);
	sb.append("\n[Created at]: " + this.created_at);
	sb.append("\n[retweet count]: " + this.retweet_count);
	sb.append("\n[favorite count]: " + this.favorite_count);
	sb.append("\n[informative]: " + this.informative);
	sb.append("\n[category]: " + this.category);
	sb.append("\n");
	return sb.toString();
    } // toString()

} // Tweet