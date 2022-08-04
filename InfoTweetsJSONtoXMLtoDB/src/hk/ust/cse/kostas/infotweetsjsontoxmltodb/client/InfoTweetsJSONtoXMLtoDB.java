package infotweetsjsontoxmltodb.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.math.BigDecimal;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.DOMException;

import org.json.*;

import java.sql.Timestamp;
import infotweetsjsontoxmltodb.util.StringDateConversion;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import infotweetsjsontoxmltodb.database.Database;
import infotweetsjsontoxmltodb.entities.Tweet;
//import infotweetsjsontoxmltodb.entities.TweetLabel;
import infotweetsjsontoxmltodb.entities.TweetStatus;
import infotweetsjsontoxmltodb.entities.User;
//import infotweetsjsontoxmltodb.database.LabelTuple;

public class InfoTweetsJSONtoXMLtoDB {

    String jsonfile;
    String xmlfile; 
    List<Tweet> tweets;
    List<User> users;
    Document dom;

    private Database database;

    public InfoTweetsJSONtoXMLtoDB(String jsonfile, String xmlfile) {
	this.tweets = new ArrayList<Tweet>();
	this.users = new ArrayList<User>();
	this.jsonfile = jsonfile; 
	this.xmlfile = xmlfile;
	this.database = new Database();
    } // InfoTweetsJSONtoXMLtoDB()

    public void run() {
	//parse the xml file and get the dom object
	System.out.println("Start parsing the xml file");
	parseXmlFile();	
	System.out.println("End parsing the xml file");
	//get each employee element and create a Employee object
	System.out.println("Parse each Tweet");
	parseTweet();	
	//Iterate through the list and print the data
	printData();	
    } // run()

    public void convertJSONToXML() {
	try {
	File file = new File(jsonfile);
	BufferedReader reader = new BufferedReader
	    (new InputStreamReader(new FileInputStream(file), "UTF-8"));

	Writer writer = new OutputStreamWriter
	    (new FileOutputStream(xmlfile), "UTF-8");
	    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    writer.write("\n");
	    writer.write("<Dataset>");
	    writer.write("\n");
	    String line;
	    //while(reader.ready()) {
	    //line = reader.readLine();
	    while((line = reader.readLine()) != null) {
		JSONObject o = new JSONObject(line);
		String xml = org.json.XML.toString(o);
		Pattern pattern = Pattern.compile("[\\p{Cntrl}|\\uFFFD]");
		Matcher m = pattern.matcher(xml);
		if(m.find()){
		    System.out.println("Control Characters found");
		    xml = m.replaceAll("");
		}
		//String s = "\u001A";
		//char c = (char)Integer.parseInt('\u001A', 8);		
		//if(xml.contains(s)) 
		//    System.out.println(s);
		    //xml.replace(s,"");
		org.json.XMLTokener tokens = new org.json.XMLTokener(xml);
		writer.write("<Tweet>");
		writer.write(xml); 
		writer.write("<informative>");
		writer.write("null");
		writer.write("</informative>");
		writer.write("<category>");
		writer.write("null");
		writer.write("</category>");
		writer.write("</Tweet>");
		writer.write("\n"); 
	    }
	    writer.write("</Dataset>");
	    reader.close();
	    writer.close();
	} catch(org.json.JSONException ex) {
	} catch(java.io.IOException ioex) {}
    } // convertJSONToXML()

    private void parseTweet(){
	// the root element
	Element docTweet = dom.getDocumentElement();
	// a nodelist of elements
	NodeList nl = docTweet.getElementsByTagName("Tweet");
	if(nl != null && nl.getLength() > 0) {
	    for(int i = 0 ; i < nl.getLength();i++) {		
		// get Tweet element
		Element el = (Element)nl.item(i);		
		// get Tweet object
		Tweet tweet = getTweet(el);		
		this.tweets.add(tweet);
		if(tweet.status == TweetStatus.RETWEET) {
		    BigDecimal retweetID = tweet.id;
		    NodeList nlRt = docTweet.getElementsByTagName
			("retweeted_status");
		    Element elRt = (Element)nlRt.item(0);  
		    Tweet retweetOriginal = getTweet(elRt);
		    BigDecimal originalID = retweetOriginal.id;
		    this.tweets.add(retweetOriginal);
		    System.out.println("[DB] Retweet: ");
		    System.out.println("Original: " + originalID 
				       + " Retweet: " + retweetID);
		    //store retweet tuple in database.
		    this.database.insertRetweetTuple(originalID,retweetID);
		}
	    }
	} else 
	    System.out.println("NULL");
    } // parseTweet()

    /**
     *
     */
    private Tweet getTweet(Element tweetEl) {
	TweetStatus status = TweetStatus.INIT;
	BigDecimal in_reply_to_status_id = 
	    getBigDecimalValue(tweetEl,"in_reply_to_status_id");
	BigDecimal in_reply_to_user_id = 
	    getBigDecimalValue(tweetEl,"in_reply_to_user_id");
	String in_reply_to_screen_name = 
	    getTextValue(tweetEl, "in_reply_to_screen_name");
	BigDecimal nullID = new BigDecimal(-1);
	NodeList nlRet = tweetEl.getElementsByTagName("retweeted_status");
	BigDecimal id = new BigDecimal("-1");	
	if(in_reply_to_status_id.compareTo(nullID) != 0) {	    
	    /*
	    NodeList idList = tweetEl.getElementsByTagName("id");
	    for(int i = 0; i < idList.getLength(); i++) {
		Node n = idList.item(i);
		if(n.getParentNode().getNodeName().equals("Tweet")) {
		    Element el = (Element)n;
		    String strID = el.getFirstChild().getNodeValue();
		    id = new BigDecimal(strID);		    
		}
		}*/
	    //id = getBigDecimalValue(tweetEl,"id");
	    status = TweetStatus.REPLY; 	  
	} else if(nlRet.getLength() > 0) {
	    status = TweetStatus.RETWEET;
	    //id = getBigDecimalValue(tweetEl,"id");
	} else {
	    status = TweetStatus.ORIGINAL;
	    //id = getBigDecimalValue(tweetEl,"id");
	}
	    NodeList idList = tweetEl.getElementsByTagName("id");
	    for(int i = 0; i < idList.getLength(); i++) {
		Node n = idList.item(i);
		if(n.getParentNode().getNodeName().equals("Tweet")) {
		    Element el = (Element)n;
		    String strID = el.getFirstChild().getNodeValue();
		    id = new BigDecimal(strID);		    
		}
	    }

	    //System.out.println(status);

	String text = getTextValue(tweetEl,"text");
	Timestamp created_at = getTimestampValue(tweetEl,"created_at");
	int retweet_count = getIntValue(tweetEl,"retweet_count");
	int favorite_count = getIntValue(tweetEl,"favorite_count");
	boolean informative = getBooleanValue(tweetEl,"informative"); 
	int category = getIntValue(tweetEl,"category");

	// mentioned users details.	
	NodeList nlUM = tweetEl.getElementsByTagName("user_mentions");
	if(nlUM != null && nlUM.getLength() > 0) {
	    for(int i = 0 ; i < nlUM.getLength(); i++) {	     
		Element elUM = (Element)nlUM.item(i);
		BigDecimal userid = getBigDecimalValue(elUM,"id");
		String name = getTextValue(elUM,"name");
		String screen_name = getTextValue(elUM,"screen_name");
				
		//System.out.println("[DB] Mentioned user: ");
		//System.out.println
		//    ("MessageID: " + id 
		//     + " UserID: " + userid
		//     + " UserName: " + name
		//     + " UserScreenName: " + screen_name);
		// store mentioned-users tuple in database
		this.database.insertMessageUsersTuple
		    (id, userid, name,screen_name);
	    }
	}

	// author details.
	System.out.println("[DB] Author User: ");
	NodeList nl = tweetEl.getElementsByTagName("user");
	BigDecimal userid = new BigDecimal("-1");
	if(nl != null && nl.getLength() > 0) {
	    for(int i = 0 ; i < nl.getLength();i++) {		
		//get the employee element
		Element el = (Element)nl.item(i);		
		//get a User object
		User user = getUser(el);		
		System.out.println("Insert User");
		System.out.println(user.toSQLInsertionString()); 
		this.database.insertUserTuple(user);
		userid = user.id;
		this.users.add(user);		
	    }
	}

	Tweet tweet = new Tweet(status, id, userid, text, created_at,
				retweet_count, favorite_count,
				informative, category);

	System.out.println("Insert Tweet");
	// insert tweet in database.
	System.out.println(tweet.toSQLInsertionString());
	this.database.insertTweetTuple(tweet);

	if(tweet.status == TweetStatus.REPLY) {
	    System.out.println("[DB] Reply: ");
	    System.out.println
		("ReplyID: " + id 
		 + " OriginalID: " + in_reply_to_status_id
		 + " OriginalUserID: " + in_reply_to_user_id
		 + " OriginalScreenName: " + in_reply_to_screen_name);
	    // store reply tuple in database
	    this.database.insertReplyTuple
		(id,in_reply_to_status_id,in_reply_to_user_id,
		 in_reply_to_screen_name);
	}
	return tweet;
    } // getTweet()

    private User getUser(Element userElement) { 
	BigDecimal id = getBigDecimalValue(userElement,"id");
	String name = getTextValue(userElement,"name");
	String screen_name = getTextValue(userElement,"screen_name");
	String description = getTextValue(userElement,"description");
	boolean verified = getBooleanValue(userElement,"verified");
	String created_at = getTextValue(userElement,"created_at");
	int followers_count = getIntValue(userElement,"followers_count");
	int friends_count = getIntValue(userElement,"friends_count");
	int statuses_count = getIntValue(userElement,"statuses_count");
	int listed_count = getIntValue(userElement,"listed_count");
	int favourites_count = getIntValue(userElement,"favourites_count");
	String url = getTextValue(userElement,"url");
	String time_zone = getTextValue(userElement,"time_zone");
	String location = getTextValue(userElement,"location");

	User user = new User(id, name, screen_name, description, verified,
			     created_at, followers_count, friends_count,
			     statuses_count, listed_count, 
			     favourites_count, url, time_zone, location);

	return user;
    } // getUser()

    /**
     *
     */
    private String getTextValue(Element ele, String tagName) {
	String textVal = null;
	try {
	    NodeList nl = ele.getElementsByTagName(tagName);
	    if(nl != null && nl.getLength() > 0) {
		Element el = (Element)nl.item(0);
		textVal = el.getFirstChild().getNodeValue();
		textVal = textVal.replace("'","");
	    }
	} catch (DOMException domex) {
	} finally {
	    return textVal; 
	}
    } // getTextValue()

    /**
     *
     */
    private boolean getBooleanValue(Element ele, String tagName) {
	String textVal = null;
	boolean boolVal = false;
	try {
	    NodeList nl = ele.getElementsByTagName(tagName);
	    if(nl != null && nl.getLength() > 0) {
		Element el = (Element)nl.item(0);
		textVal = el.getFirstChild().getNodeValue();
		if(textVal.equals("true"))
		    boolVal = true;
		else
		    boolVal = false;
	    }
	} catch (DOMException domex) {
	} finally {
	    return boolVal; 
	}
    } // getBooleanValue()

    private BigDecimal getBigDecimalValue(Element ele, String tagName) {
	String textVal = "";
	BigDecimal value = null;
	try {
	    NodeList nl = ele.getElementsByTagName(tagName);
	    if(nl != null && nl.getLength() > 0) {
		Element el = (Element)nl.item(0);
		textVal = el.getFirstChild().getNodeValue();
		if(!textVal.equals("null"))
		    value = new BigDecimal(textVal);
		else
		    value = new BigDecimal(-1);
	    }
	} catch (DOMException domex) {
	} finally {
	    return value; 
	}
    } // getBooleanValue()

    private Timestamp getTimestampValue(Element ele, String tagName) {
	NodeList nl = ele.getElementsByTagName(tagName);
	if(nl != null && nl.getLength() > 0) {
	    Element el = (Element)nl.item(0);
	    String strCreated = el.getFirstChild().getNodeValue();
	    StringDateConversion sdCreated = 
		new StringDateConversion(strCreated);
	    return (sdCreated.toSQLTimestamp());
	}
	return null;
    } // getTimestampValue()

    /**
     *
     */
    private int getIntValue(Element ele, String tagName) {
	int value = -1;
	try{ 
	    value = Integer.parseInt(getTextValue(ele,tagName));
	} catch (DOMException domex) {
	} finally {
	    return value; 
	}
    } // getIntValue()

    private void printData(){
	/*	System.out.println("No of Tweets: " + this.tweets.size());
	Iterator tweetIterator = this.tweets.iterator();
	while(tweetIterator.hasNext()) {
	    System.out.println(tweetIterator.next().toString());
	}
	
	System.out.println("No of Users: " + this.users.size());
	Iterator userIterator = this.users.iterator();
	while(userIterator.hasNext()) {
	    System.out.println(userIterator.next().toString());
	    }*/
	System.out.println("No of Tweets: " + this.tweets.size());
	System.out.println("No of Users: " + this.users.size());
    } // printData()

    private void parseXmlFile(){
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    dom = db.parse(xmlfile);
	}catch(ParserConfigurationException pce) {
	    pce.printStackTrace();
	}catch(SAXException se) {
	    se.printStackTrace();
	}catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    } // parseXmlFile()


    /*
     * LabelTuple is a tuple from the labels relation.
     * This is a relation taken from another database.
     * It was not initially created by this project. 
     *
    public void mergeLabels() {
	List<LabelTuple> labels = this.database.extractTuples();
	List<Tweet> tweets = this.database.extractTweetsTuples();
	
	int counter = 0;
	for(LabelTuple label : labels) {
	    String m = label.message;
	    for(Tweet tweet : tweets) {
		if(tweet.text.contains(m)) {
		    TweetLabel tl = new TweetLabel
			(tweet, label.informative, label.category);
		    this.database.insertTweetLabelTuple(tl);
		    counter++;
		}
	    }
	}
	System.out.println(counter);
    } // mergeLabels()
*/
    public static void main(String[] args) {
	String jsonfile = args[1];
	String xmlfile = args[2];
	String mode = args[3];
	InfoTweetsJSONtoXMLtoDB project 
	    = new InfoTweetsJSONtoXMLtoDB(jsonfile, xmlfile);
	if(mode.equals("createXML"))
	    project.convertJSONToXML();
	else if(mode.equals("extractEntities"))
	    project.run();
	//else if(mode.equals("mergeLabels"))
	//  project.mergeLabels();
    } // main()

} // InfoTweetsJSONParser