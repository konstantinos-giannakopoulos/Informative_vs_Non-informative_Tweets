/*
 * Created on February 25, 2015 by kostas-κγ 
 *
 * This is part of the InfoTweetsLexical project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetslexical.lexical;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import lexical.Jaws;
import infotweetslexical.lexical.Stemmer;

public class Fixer {
    private List<String> noTerms;
    private List<String> stopwords;

    public Fixer() {
	this.noTerms = new ArrayList<String>();
	this.stopwords = new ArrayList<String>();
	initTerms();
    } // Tokenizer()

    private void initTerms() {       
	this.noTerms.add("wtf");        this.noTerms.add("lmao");	
	this.noTerms.add("hm");	        this.noTerms.add("oh"); 	
	this.noTerms.add("wow"); 	this.noTerms.add("brb");
	this.noTerms.add("btw"); 	this.noTerms.add("bff"); 	
	this.noTerms.add("ily");        this.noTerms.add("cya"); 	
	this.noTerms.add("cu"); 	this.noTerms.add("lol");
	this.noTerms.add("pov"); 	this.noTerms.add("rt"); 	
	this.noTerms.add("xoxo");
	this.noTerms.add("tmi"); 	this.noTerms.add("thx"); 	
	this.noTerms.add("omg");
	this.noTerms.add("omfg"); 	this.noTerms.add("idk"); 	
	this.noTerms.add("xx");
	this.noTerms.add("shhhh"); 	this.noTerms.add("ha"); 	
	this.noTerms.add("aha");
	this.noTerms.add("ahaha"); 	this.noTerms.add("haha"); 	
	this.noTerms.add("hihihihi");
	this.noTerms.add("hahaha");     this.noTerms.add("hihi");       
	this.noTerms.add("hhahha");
	this.noTerms.add("hahha");      this.noTerms.add("hmmm");       
	this.noTerms.add("aka");
	this.noTerms.add("heh");        this.noTerms.add("dah");        
	this.noTerms.add("awwww");
	this.noTerms.add("awww"); 	this.noTerms.add("awh");        
	this.noTerms.add("smh");  
	this.noTerms.add("yo");         this.noTerms.add("hun");        
	this.noTerms.add("asf");
	this.noTerms.add("lmfao");     	this.noTerms.add("ixoxo"); 	
	this.noTerms.add("ugh");     	this.noTerms.add("ops"); 	
	this.noTerms.add("xo");
	this.noTerms.add("xxx");        this.noTerms.add("xxxx");       
	this.noTerms.add("st");
	this.noTerms.add("co");         this.noTerms.add("con");        
	this.noTerms.add("com");
	this.noTerms.add("lillili");    this.noTerms.add("lmaoo");      
	this.noTerms.add("doa");
	this.noTerms.add("tbh");        this.noTerms.add("wdf");       
	this.noTerms.add("ahh"); 
	this.noTerms.add("idkk");       this.noTerms.add("woah");   
	this.noTerms.add("ouch");
	this.noTerms.add("ew");         this.noTerms.add("muah");    
	this.noTerms.add("duh");

	/* stopwords */
	/*
	this.stopwords.add("i"); this.stopwords.add("me");
	this.stopwords.add("my"); */
	
	this.stopwords.add("i"); this.stopwords.add("you");
	this.stopwords.add("to"); this.stopwords.add("the");
	this.stopwords.add("and"); this.stopwords.add("it");
	this.stopwords.add("my"); this.stopwords.add("my");
	this.stopwords.add("me"); this.stopwords.add("is");
	this.stopwords.add("in"); this.stopwords.add("for");
	this.stopwords.add("of"); this.stopwords.add("am");
	this.stopwords.add("that"); this.stopwords.add("on");
	this.stopwords.add("your"); this.stopwords.add("be");
	this.stopwords.add("this"); this.stopwords.add("so");
	this.stopwords.add("have"); this.stopwords.add("just");
	this.stopwords.add("with"); this.stopwords.add("like");
	this.stopwords.add("all"); this.stopwords.add("not");
	this.stopwords.add("get"); this.stopwords.add("are");
	this.stopwords.add("at"); this.stopwords.add("no");
	this.stopwords.add("if"); this.stopwords.add("do");
	this.stopwords.add("want"); this.stopwords.add("what");
	this.stopwords.add("but"); this.stopwords.add("dont");
	this.stopwords.add("when"); this.stopwords.add("can");
	this.stopwords.add("we"); this.stopwords.add("was");
	this.stopwords.add("how"); this.stopwords.add("cannot");
	this.stopwords.add("about"); this.stopwords.add("by");
	this.stopwords.add("will"); this.stopwords.add("who");
	this.stopwords.add("they"); this.stopwords.add("got");
	this.stopwords.add("from"); this.stopwords.add("or");
	this.stopwords.add("too"); this.stopwords.add("more");
	this.stopwords.add("as"); this.stopwords.add("he");
	this.stopwords.add("much");

	/* used *
	this.stopwords.add("the");	this.stopwords.add("and"); 
	this.stopwords.add("a");        this.stopwords.add("an"); 
	this.stopwords.add("to");       this.stopwords.add("up");*/
	
	/*this.stopwords.add("for");	this.stopwords.add("on");
	this.stopwords.add("for");    this.stopwords.add("not");
	this.stopwords.add("in");       this.stopwords.add("at");
	this.stopwords.add("of");       this.stopwords.add("off");
	this.stopwords.add("on");       this.stopwords.add("with");
	*/

	   /*
	//most common

	this.stopwords.add("to"); 
	this.stopwords.add("my");       this.stopwords.add("it"); 
	this.stopwords.add("for");      this.stopwords.add("of");       this.stopwords.add("is");
	this.stopwords.add("in");       this.stopwords.add("me");       this.stopwords.add("that");
	this.stopwords.add("on");       this.stopwords.add("are");      this.stopwords.add("this");
	this.stopwords.add("at");       this.stopwords.add("have");     this.stopwords.add("like");
	this.stopwords.add("can");      this.stopwords.add("be");       this.stopwords.add("just");
	this.stopwords.add("so");       this.stopwords.add("your");     this.stopwords.add("with");
	this.stopwords.add("not");      this.stopwords.add("what");     this.stopwords.add("we");
	this.stopwords.add("up");       this.stopwords.add("do");       this.stopwords.add("but");
	this.stopwords.add("dont");     this.stopwords.add("one");      this.stopwords.add("get");
	this.stopwords.add("will");     this.stopwords.add("all");      this.stopwords.add("if"); 
	this.stopwords.add("unless");   
	this.stopwords.add("no");       this.stopwords.add("new");      this.stopwords.add("now");
	this.stopwords.add("like");     this.stopwords.add("want");     this.stopwords.add("when");
	this.stopwords.add("was");      this.stopwords.add("out");      this.stopwords.add("by");
	this.stopwords.add("about");	this.stopwords.add("are");	this.stopwords.add("dont");
	this.stopwords.add("ie");	this.stopwords.add("how");	this.stopwords.add("got");
	this.stopwords.add("they");	this.stopwords.add("who");	this.stopwords.add("too");
	this.stopwords.add("our");	this.stopwords.add("why");	this.stopwords.add("her");
	this.stopwords.add("its");	this.stopwords.add("she");	this.stopwords.add("or");
	this.stopwords.add("am");	this.stopwords.add("some");	this.stopwords.add("us");
	this.stopwords.add("without");	this.stopwords.add("off");	this.stopwords.add("he");
	this.stopwords.add("have");	this.stopwords.add("then");	this.stopwords.add("them");
	this.stopwords.add("always");	this.stopwords.add("never");	this.stopwords.add("as");
	this.stopwords.add("didnt");	this.stopwords.add("were");	this.stopwords.add("aint");
	this.stopwords.add("here");	this.stopwords.add("being");	this.stopwords.add("has");
	this.stopwords.add("did");	this.stopwords.add("doesnt");	this.stopwords.add("his");
	this.stopwords.add("ok");	this.stopwords.add("hi");	this.stopwords.add("very");
	this.stopwords.add("a");	this.stopwords.add("an");	this.stopwords.add("had");
	this.stopwords.add("doesnt");	this.stopwords.add("so");	this.stopwords.add("such");
	this.stopwords.add("okay");     this.stopwords.add("well");     
	//negations
	this.stopwords.add("isnt");	this.stopwords.add("havent");	this.stopwords.add("shouldnt");
	this.stopwords.add("arent");	this.stopwords.add("wasnt");	this.stopwords.add("dont");
	this.stopwords.add("couldnt"); 	this.stopwords.add("wouldnt");
	this.stopwords.add("cannot"); 	this.stopwords.add("cant"); 	

	this.stopwords.add("throughout"); this.stopwords.add("over"); this.stopwords.add("because");	
	this.stopwords.add("really");this.stopwords.add("even");

	this.stopwords.add("anyway");   this.stopwords.add("another");this.stopwords.add("other");
	this.stopwords.add("everybody"); this.stopwords.add("nobody"); this.stopwords.add("somebody");
	this.stopwords.add("anybody");
	this.stopwords.add("nevermind"); this.stopwords.add("whatever"); this.stopwords.add("noone");
	this.stopwords.add("anyone");   this.stopwords.add("someone");  this.stopwords.add("everyone");  
	this.stopwords.add("anyones");  this.stopwords.add("someones"); this.stopwords.add("everyones");
	
	this.stopwords.add("much");     this.stopwords.add("many");  this.stopwords.add("more");
	this.stopwords.add("only");     this.stopwords.add("still");  this.stopwords.add("every");
  	this.stopwords.add("any");      this.stopwords.add("should"); this.stopwords.add("would");
	this.stopwords.add("might");

	this.stopwords.add("follow");     this.stopwords.add("followers"); this.stopwords.add("following");
	this.stopwords.add("unfollowers");this.stopwords.add("follower");  this.stopwords.add("unfollower");
	this.stopwords.add("unfollow");   this.stopwords.add("unfollowed");this.stopwords.add("followed");

	this.stopwords.add("sometimes"); this.stopwords.add("anytime");
	this.stopwords.add("something"); this.stopwords.add("anything"); this.stopwords.add("nothing");
	this.stopwords.add("everything"); this.stopwords.add("thing"); this.stopwords.add("thing");
	this.stopwords.add("none");   this.stopwords.add("thank");
	this.stopwords.add("these");    this.stopwords.add("those");    this.stopwords.add("this"); 
	this.stopwords.add("that");     this.stopwords.add("thing");     
	this.stopwords.add("need");    this.stopwords.add("yes");
	this.stopwords.add("no");       this.stopwords.add("maybe");   this.stopwords.add("may");
	this.stopwords.add("shall");    this.stopwords.add("from");    
	this.stopwords.add("nevertheles");this.stopwords.add("although");this.stopwords.add("however");
	//nouns
	this.stopwords.add("morning");this.stopwords.add("birthday");this.stopwords.add("night");
	//pronouns
	this.stopwords.add("him"); this.stopwords.add("her"); this.stopwords.add("your");
	this.stopwords.add("mine"); this.stopwords.add("yours"); this.stopwords.add("his");
	this.stopwords.add("hers"); this.stopwords.add("their"); this.stopwords.add("our");
	this.stopwords.add("ours"); this.stopwords.add("theirs");this.stopwords.add("my");
	
	this.stopwords.add("either");this.stopwords.add("or");  
	this.stopwords.add("neither");this.stopwords.add("nor");  
	this.stopwords.add("for");this.stopwords.add("against");
	this.stopwords.add("all");this.stopwords.add("none");    

	this.stopwords.add("than");this.stopwords.add("aswell");
	//
	this.stopwords.add("already");this.stopwords.add("almost");this.stopwords.add("also");
	this.stopwords.add("too");this.stopwords.add("only");
	this.stopwords.add("usually");this.stopwords.add("yet");this.stopwords.add("till");
	this.stopwords.add("until");this.stopwords.add("even");this.stopwords.add("ago");
	//quantity
	this.stopwords.add("lot"); this.stopwords.add("alot"); 
	this.stopwords.add("same"); this.stopwords.add("other"); 
	this.stopwords.add("most");this.stopwords.add("much");this.stopwords.add("many");
	this.stopwords.add("few"); this.stopwords.add("enough");this.stopwords.add("less");
	this.stopwords.add("more");
    
	this.stopwords.add("into"); this.stopwords.add("dude");

	this.stopwords.add("myself"); this.stopwords.add("yourself"); this.stopwords.add("himself");
	this.stopwords.add("herself"); 	this.stopwords.add("ourselves"); 
	this.stopwords.add("yourselves");
	this.stopwords.add("theirselves");
	//wh
	this.stopwords.add("where"); this.stopwords.add("when");  
	this.stopwords.add("which"); this.stopwords.add("while");
	this.stopwords.add("must");	
	//verbs
	this.stopwords.add("go");   this.stopwords.add("love");this.stopwords.add("fuck");
	this.stopwords.add("make"); this.stopwords.add("see"); this.stopwords.add("know");
	this.stopwords.add("say");  this.stopwords.add("think"); this.stopwords.add("feel");
	this.stopwords.add("love"); this.stopwords.add("look"); this.stopwords.add("watch");
	this.stopwords.add("miss"); this.stopwords.add("wish"); this.stopwords.add("hate");
	this.stopwords.add("mean"); this.stopwords.add("tell"); this.stopwords.add("there");
	this.stopwords.add("hope"); this.stopwords.add("their"); this.stopwords.add("take");
	this.stopwords.add("give"); this.stopwords.add("get"); this.stopwords.add("put"); 
	this.stopwords.add("want"); this.stopwords.add("try");this.stopwords.add("care");
	this.stopwords.add("stay"); this.stopwords.add("matter");this.stopwords.add("wait");       
	this.stopwords.add("use");  this.stopwords.add("bring");this.stopwords.add("bless");
	this.stopwords.add("work");  this.stopwords.add("want");this.stopwords.add("learn");
	//adjectives
	this.stopwords.add("difficult");this.stopwords.add("easy"); this.stopwords.add("awesome"); 
	this.stopwords.add("sort");this.stopwords.add("long"); 
	this.stopwords.add("happy"); this.stopwords.add("sad");this.stopwords.add("nice");
	//time
	this.stopwords.add("early"); this.stopwords.add("late");this.stopwords.add("since");
	this.stopwords.add("yesterday"); this.stopwords.add("today");this.stopwords.add("tomorrow");
	this.stopwords.add("previously"); this.stopwords.add("later");this.stopwords.add("early"); 
	this.stopwords.add("after");
	this.stopwords.add("previous"); this.stopwords.add("next");this.stopwords.add("from");
	this.stopwords.add("back");this.stopwords.add("front");
	this.stopwords.add("past");this.stopwords.add("future");
	this.stopwords.add("exactly");this.stopwords.add("actually");this.stopwords.add("actual");
	//distance
	this.stopwords.add("away");this.stopwords.add("towards");this.stopwords.add("away");
	this.stopwords.add("furthest");
	this.stopwords.add("together");this.stopwords.add("alone");this.stopwords.add("far");   
	//order
	this.stopwords.add("first");this.stopwords.add("second");this.stopwords.add("third");
	this.stopwords.add("fourth");this.stopwords.add("last");this.stopwords.add("good morning");


	*/
    }

    public String fix(String token) {

	//if(token.startsWith("@") || token.contains("@")) return null;
	//if(token.startsWith("http:")) return null;
	//if(token.startsWith("https:")) return null;
	
	if(token.contains("ooo"))
	    token = token.replaceAll("o{2,}","o");
	if(token.contains("eee"))
	    token = token.replaceAll("e{2,}","e");
	if(token.contains("sss"))
	    token = token.replaceAll("s{2,}","s");
	if(token.contains("hhh"))
	    token = token.replaceAll("h{2,}","h");
	if(token.contains("iii"))
	    token = token.replaceAll("i{2,}","i");
	if(token.contains("yyy"))
	    token = token.replaceAll("y{2,}","y");
	if(token.contains("aaa"))
	    token = token.replaceAll("a{2,}","a");
	if(token.contains("rrr"))
	    token = token.replaceAll("r{2,}","r");
	if(token.contains("lll"))
	    token = token.replaceAll("l{2,}","l");
	if(token.contains("ttt"))
	    token = token.replaceAll("t{2,}","t");
	if(token.contains("uuu"))
	    token = token.replaceAll("u{2,}","u");
	if(token.contains("nnn"))
	    token = token.replaceAll("n{2,}","n");
	if(token.contains("ddd"))
	    token = token.replaceAll("d{2,}","d");
	if(token.contains("ggg"))
	    token = token.replaceAll("g{2,}","g");

	if(token.equals("ll"))
	    token = "will";
	else if(token.equals("re"))
	    token = "are";
	else if(token.equals("ve"))
	    token = "have";
	else if(token.equals("shoulda"))
	    token = "should";
	else if(token.equals("gonna") || token.equals("goin"))
	    token = "going";
	else if(token.equals("wanna"))
	    token = "want";
	else if(token.equals("gotta") || token.equals("gotten"))
	    token = "got";
	else if(token.equals("im"))
	    token = "am";
	else if(token.equals("ive"))
	    token = "have";
	//negations
	if(token.equals("don") || token.equals("dnt"))
	    token = "dont";
	else if(token.equals("haven"))
	    token = "havent";
	else if(token.equals("wasn"))
	    token = "wasnt";
	else if(token.equals("wouldn"))
	    token = "wouldnt";
	else if(token.equals("couldn"))
	    token = "couldnt";
	else if(token.equals("shouldn"))
	    token = "shouldnt";
	else if(token.equals("doesn"))
	    token = "doesnt";
	else if(token.equals("didn"))
	    token = "didnt";
	else if(token.equals("ain"))
	    token = "aint";
	else if(token.equals("aren"))
	    token = "arent";
	else if(token.equals("isn"))
	    token = "isnt";
	else if(token.equals("cant"))
	    token = "cannot";
	//	
	if(token.equals("ur"))
	    token = "your";
	else if(token.equals("cal"))
	    token = "girl";
	else if(token.equals("yall"))
	    token = "all";
	else if(token.equals("tha") || token.equals("da"))
	    token = "the";
	else if(token.equals("gud"))
	    token = "good";
	else if(token.equals("gudnight"))
	    token = "goodnight";
	else if(token.equals("gudmorning"))
	    token = "goodmorning";
	else if(token.equals("ya") || token.equals("yu") || token.equals("u"))
	    token = "you";
	else if(token.equals("sry"))
	    token = "sorry";
	else if(token.equals("thru"))
	    token = "through";
	else if(token.equals("tho"))
	    token = "though";
	else if(token.equals("pple"))
	    token = "people";
	else if(token.equals("hey") || token.equals("hello") || token.equals("hii"))
	    token = "hi";
	else if(token.equals("ma"))
	    token = "may";
	else if(token.equals("needa"))
	    token = "need";
	else if(token.equals("ima"))
	    token = "maybe";
	else if(token.equals("em"))
	    token = "them";
	else if(token.equals("bby"))
	    token = "baby";
	else if(token.equals("ths"))
	    token = "this";
	else if(token.equals("witchu"))
	    token = "with";
	else if(token.equals("cha"))
	    token = "you";
	else if(token.equals("mtg"))
	    token = "meeting";
	else if(token.equals("noo") || token.equals("nah"))
	    token = "no";
	else if(token.equals("soo"))
	    token = "so";
	else if(token.equals("hoe") || token.equals("hoes"))
	    token = "whore";
	else if(token.equals("wht") || token.equals("wut") || token.equals("whats") || token.equals("whatt") || token.equals("wot") || token.equals("wots") || token.equals("whatta")) 
	    token = "what";
	else if(token.equals("sch"))
	    token = "such";
	else if(token.equals("thanks") || token.equals("thankyou") || token.equals("thnx") || token.equals("thnks"))
	    token = "thank";
	else if(token.equals("plz"))
	    token = "please";
	else if(token.equals("be4"))
	    token = "before";
	else if(token.equals("cuz") || token.equals("coz"))
	    token = "because";
	else if(token.equals("dunno"))
	    token = "know";
	else if(token.equals("sayin"))
	    token = "saying";
	else if(token.equals("thats") || token.equals("tht") || token.equals("dat"))
	    token = "that";
	else if(token.equals("yrs") || token.equals("years"))
	    token = "year";
	else if(token.equals("idk"))
	    token = "know";
	else if(token.equals("ppl"))
	    token = "people";
	else if(token.equals("tbh"))
	    token = "honest";
	else if(token.equals("yeah")||token.equals("yea")||token.equals("yep")||token.equals("yo")||token.equals("yeeah"))
	    token = "yes";
	/*
	//tenses
	if(token.equals("does") || token.equals("doing") || token.equals("did") || token.equals("done") || token.equals("doin"))
	    token = "do";
	else if(token.equals("went") || token.equals("gone") || token.equals("going") || token.equals("goes") || token.equals("goin"))
	    token = "go";
	else if(token.equals("had") || token.equals("has") || token.equals("having") || token.equals("havin"))
	    token = "have";
	else if(token.equals("able") || token.equals("could") || token.equals("couldve"))
	    token = "can";
	else if(token.equals("am")||token.equals("was") || token.equals("is") || token.equals("were") || token.equals("been"))
	    token = "be";
	else if(token.equals("made") || token.equals("makes") || token.equals("making") || token.equals("makin"))
	    token = "make";
	else if(token.equals("takes") || token.equals("took") || token.equals("taking") || token.equals("taken"))
	    token = "take";
	else if(token.equals("gives") || token.equals("gave") || token.equals("giving") || token.equals("given"))
	    token = "give";
	else if(token.equals("said") || token.equals("saying") || token.equals("says") || token.equals("sayin"))
	    token = "say";
	else if(token.equals("seen") || token.equals("saw") || token.equals("sees"))
	    token = "see";
	else if(token.equals("told") || token.equals("tells") || token.equals("telling") || token.equals("tellin"))
	    token = "tell";
	else if(token.equals("asks") || token.equals("asked") || token.equals("asking"))
	    token = "ask";
	else if(token.equals("tries") || token.equals("tried") || token.equals("trying") || token.equals("tryin"))
	    token = "try";
	else if(token.equals("likes") || token.equals("liked") || token.equals("liking") || token.equals("likin"))
	    token = "like";
	else if(token.equals("loves") || token.equals("loved") || token.equals("loving") || token.equals("lovin"))
	    token = "love";
	else if(token.equals("watches") || token.equals("watched") || token.equals("watching") || token.equals("watchin"))
	    token = "watch";
	else if(token.equals("wants") || token.equals("wanted"))
	    token = "want";
	else if(token.equals("works") || token.equals("worked") || token.equals("working"))
	    token = "work";
	else if(token.equals("learnss") || token.equals("learnt") || token.equals("learning"))
	    token = "learn";

	//
	if(token.equals("good") || token.equals("better") || token.equals("best"))
	    token = "good";
	else if(token.equals("bad") || token.equals("worse") || token.equals("worst"))
	    token = "bad";/

*/
	if(token.equals("I") || token.equals("i"))
	    return token;

	if(token.equals("")) return "";
	if(token.length() < 2) return "";
	if(this.noTerms.equals(token)) return "";
	//if(this.stopwords.contains(token)) return ""; 

	return token;
    } // fix()

} // Fixer