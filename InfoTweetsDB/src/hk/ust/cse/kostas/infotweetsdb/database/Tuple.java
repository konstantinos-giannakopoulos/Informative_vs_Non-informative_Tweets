package hk.ust.cse.kostas.infotweetsdb.database;

import java.math.BigDecimal;

public class Tuple {

    public BigDecimal messageID;
    public BigDecimal authorID;
    public String message;
    public boolean informative;
    public int category;
    public boolean isReply;
    public BigDecimal replyToMessageID;
    public BigDecimal replyToAuthorID;
    public boolean isRetweet;
    public BigDecimal retweetFromMessageID;
    public BigDecimal retweetFromAuthorID;
    
    public Tuple() {
    
    }

    public String toSQLInsertionString() {
	String sqlInsert = this.messageID + "," + this.authorID + ",'" 
	    + this.message + "'," + this.informative + "," 
	    + this.category + "," 
	    + this.isReply + "," 
	    + this.replyToMessageID + "," 
	    + this.replyToAuthorID + "," 
	    + this.isRetweet + "," 
	    + this.retweetFromMessageID + "," 
	    + this.retweetFromAuthorID 
	    ; 
	return sqlInsert;
    } // toSQLInsertionString()

    @Override public String toString() {
	return "\n[message ID]: " + this.messageID
	    + "\n[author ID]: " + this.authorID
	    + "\n[informative]: " + this.informative
	    + "\n[category]: " + this.category
	    + "\n[reply]: " + this.isReply
	    + "\n[reply to message]: " + this.replyToMessageID 
	    + "\n[reply to author]: " + this.replyToAuthorID 
	    + "\n[retweet]: " + this.isRetweet
	    + "\n[retweet from message]: " + this.retweetFromMessageID 
	    + "\n[retweet from author]: " + this.retweetFromAuthorID 
	    ;
    } // toString()

} // Tuple