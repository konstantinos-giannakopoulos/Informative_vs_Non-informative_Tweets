package infotweetsjsontoxmltodb.entities;

import java.math.BigDecimal;

public class User {
    public BigDecimal id;
    public String name;
    public String screen_name;
    public String description;
    public boolean verified;
    public String created_at;
    public int followers_count;
    public int friends_count;
    public int statuses_count;
    public int listed_count;
    public int favourites_count;
    public String url;
    public String time_zone;
    public String location;

    public User(BigDecimal id, String name, String screen_name,
		String description, boolean verified, String created_at,
		int followers_count, int friends_count, int statuses_count,
		int listed_count, int favourites_count, String url,
		String time_zone, String location) {
	this.id = id;
	this.name = name;
	this.screen_name = screen_name; 
	this.description = description; 
	this.verified = verified;
	this.created_at = created_at;
	this.followers_count = followers_count; 
	this.friends_count = friends_count;
	this.statuses_count = statuses_count;
	this.listed_count = listed_count;
	this.favourites_count = favourites_count; 
	this.url = url;
	this.time_zone = time_zone;
	this.location = location;
    } // User()

    public String toSQLInsertionString() {
	String sqlInsert = this.id + ",'" + this.name + "','" 
	    + this.screen_name + "','" + this.description + "','" 
	    + this.verified + "','" + this.created_at + "'," 
	    + this.followers_count + "," + this.friends_count + "," 
	    + this.statuses_count + "," + this.listed_count + ","
	    + this.favourites_count + ",'" + this.url + "','" 
	    + this.time_zone + "','" + this.location + "'"
	    ; 
	return sqlInsert;
    } // toSQLInsertionString()

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(" - User Details - ");
	sb.append("\n[ID]: " + this.id);
	sb.append("\n[name]: " + this.name);
	sb.append("\n[screen_name]: " + this.screen_name);
	sb.append("\n[description]: " + this.description);
	sb.append("\n[verified]: " + this.verified);
	sb.append("\n[created_at]: " + this.created_at);
	sb.append("\n[followers_count]: " + this.followers_count);
	sb.append("\n[friends_count]: " + this.friends_count);
	sb.append("\n[Statuses Count]: " + this.statuses_count);
	sb.append("\n[listed_count]: " + listed_count);
	sb.append("\n[favourites_count]: " + this.favourites_count);
	sb.append("\n[url]: " + this.url);
	sb.append("\n[time_zone]: " + this.time_zone);
	sb.append("\n[Location]: " + this.location);
	sb.append("\n");
	return sb.toString();
    } // toString()

} // User