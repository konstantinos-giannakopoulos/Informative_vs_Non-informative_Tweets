/*
 * Created on February 20, 2015 by kostas-κγ   
 *
 * This is part of the InfoTweets project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package hk.ust.cse.kostas.infotweets.client;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import hk.ust.cse.kostas.infotweetsdb.database.Database;
import hk.ust.cse.kostas.infotweetsdb.database.Tuple;
import hk.ust.cse.kostas.infotweetsdb.util.ProjectProperties;

import infotweetsalgorithms.input.datapoint.Datapoint;
import infotweetsalgorithms.client.InfoTweetsAlgorithms;
import infotweetsalgorithms.input.document.Document;

import infotweetslexical.client.InfoTweetsLexical;
import infotweetslexical.lexical.Tokenizer; 
import infotweetslexical.lexical.Trimmer;
import infotweetslexical.lexical.Fixer;

import hk.ust.cse.kostas.infotweets.inputParser.XMLParser;
import hk.ust.cse.kostas.infotweets.features.Features;
/**
 *
 *
 * @author kostas-κγ   
 */
public class InfoTweets {

    Database database;

    private static final InfoTweets INSTANCE = new InfoTweets();
    private InfoTweets() {}
    public static InfoTweets getInstance() {return INSTANCE;}

    public void execute(ProjectProperties properties, String mode,
			String clusteringChoice, int k,
			String classificationChoice,
			String textClassifierChoice,
			int cvPartitions,
			double weightFreqHashtags, double weightFreqWords,
			double weightFreqUsers, double weightSmoothHashtags,
			double weightSmoothWords, double weightSmoothUsers) {
	///* A) Database This is the correct code *
	//this.database = new Database(properties);
	//saveDatabase(this.database);
	//List<Tuple> tuples = loadTuplesFromDatabase(this.database);
	//System.out.println(tuples.size());
	//*/

	// B) Parsing XML file
	XMLParser parser = new XMLParser(); 
	List<Tuple> tuples = parser.readXMLFileWithTweets();
	System.out.println(tuples.size());
	//System.exit(0);
	List<Document> textDocuments = extractDocumentsFromTuples(tuples);
	InfoTweetsAlgorithms algs = 
	    new InfoTweetsAlgorithms(null, textDocuments);
	algs.run(mode,clusteringChoice, k,
		 classificationChoice,textClassifierChoice,cvPartitions,
		 weightFreqHashtags, weightFreqWords, weightFreqUsers,
		 weightSmoothHashtags,weightSmoothWords,weightSmoothUsers);
	//System.exit(0);

	/*
	/////////////////////////////////////////////////////////////////////
	// C) This code is a total mess. The above is the correct one. 
	// It's used for the second dataset only.
	//
	List<Tuple> tuples = readSecondDatasetFromFile();
	List<Document> textDocuments = extractDocumentsFromTuples(tuples);
	InfoTweetsAlgorithms algs = 
	    new InfoTweetsAlgorithms(null, textDocuments);
	algs.run(mode,clusteringChoice, k,
		 classificationChoice,textClassifierChoice,cvPartitions,
		 weightFreqHashtags, weightFreqWords, weightFreqUsers,
		 weightSmoothHashtags,weightSmoothWords,weightSmoothUsers);
	//
	//
	/////////////////////////////////////////////////////////////////////
	*/
	
	/**/
	// used for clustering
	//List<Features> listOfFeatures = extractFeaturesFromTuples(tuples);
	/* features to Datapoints */
	//List<Datapoint> listOfDatapoints = 
	//  transformToDatapoints(listOfFeatures);

	/* used in text classification. */
	//List<Document> textDocuments = extractDocumentsFromTuples(tuples);
	
	/* used in Bayesian Network classification */
	//List<Document> bayesDocuments = 
	//  extractDocumentsFromFeatures(listOfFeatures);
	/* used in Hybrid */
	//List<Document> documents = 
	//  mergeDocuments(textDocuments,bayesDocuments);
	/*
	InfoTweetsAlgorithms algs = 
	    new InfoTweetsAlgorithms(null,textDocuments);
	algs.run(mode,clusteringChoice, k,
		 classificationChoice,textClassifierChoice,cvPartitions,
		 weightFreqHashtags, weightFreqWords, weightFreqUsers,
		 weightSmoothHashtags,weightSmoothWords,weightSmoothUsers);
	*/
    } // execute()

    /**
     * Used in Hybrid
     */
    private List<Document> mergeDocuments
	(List<Document> textDocuments, List<Document> bayesDocuments) {
	List<Document> documents = new ArrayList<Document>(); 
	for(Document textDoc : textDocuments) {
	    for(Document bayesDoc : bayesDocuments) {
		if(textDoc.id.equals(bayesDoc.id)) {
		    //System.out.println(bayesDoc.id);
		    //System.out.println(textDoc.id);
		    //System.out.println(textDoc.tokens);
		    //System.out.println("");
		    documents.add
			(new Document(textDoc.id,textDoc.tokens,
			     bayesDoc.values, textDoc.labelInformative));
		}
	    }
	}
	return documents;
    } // extractDocuments()
                                                                        
    /**
     * Used in BayesianNework
     */
    private List<Document> extractDocumentsFromFeatures
	(List<Features> listOfFeatures) {
	List<Document> documents = new ArrayList<Document>();
	for(Features feat : listOfFeatures) {
	    int [] values = new int[4];
	    BigDecimal id = feat.id;
	    boolean labelInformative = feat.labelInformative;
	    values[0] = feat.iWords; 
	    values[1] = feat.iHashtags;
	    values[2] = (feat.bUrl) ? 1 : 0;  
 	    values[3] = feat.iRecipients; 
	    documents.add(new Document(id,values,labelInformative));
	}
	return documents;
    } // extractDocumentsForBayesianNetwork()
    
    private List<Tuple> loadTuplesFromDatabase(Database database) {
	return this.database.extractTuples();	
    } // loadDatabase()

    /**
     * Used in TextClassification.
     * Tokenize document with lexical.Tokenizer
     */
    private List<Document> extractDocumentsFromTuples(List<Tuple> tuples) {
	InfoTweetsLexical lexical = new InfoTweetsLexical();

	List<Document> documents = new ArrayList<Document>();
	for(Tuple tuple : tuples) {
	    //System.out.println(tuple);
	    BigDecimal id = tuple.messageID;
	    boolean labelInformative = tuple.informative;

	    List<String> tokens = new ArrayList<String>();
	    String message = tuple.message;
	    tokens.addAll(lexical.process(message));

	    //System.out.println("Tokens: " + tokens);
	    documents.add(new Document(id, tokens, labelInformative));	 
	}
	return documents;
    } // extractDocumentsFromTuples()

    private List<Features> extractFeaturesFromTuples(List<Tuple> tuples) {
	List<Features> listOfFeatures = new ArrayList<Features>();
	for(Tuple tuple : tuples) {
	    //System.out.println(tuple);
	    Features features = new Features();
	    features.id = tuple.messageID;
	    features.labelInformative = tuple.informative;
	    int numWords = 0;
	    int numHashtags = 0;
	    int numRecipients = 0;
	    boolean hasUrl = false;
	    String message = tuple.message;
	    String delimiters = " ";
	    StringTokenizer messageTokens = 
		new StringTokenizer(message,delimiters);
	    while(messageTokens.hasMoreTokens()) {
		String token = messageTokens.nextToken();
		if(token.startsWith("http"))
		    hasUrl = true;
		else if(token.startsWith("#"))
		    numHashtags++;
		else if(token.startsWith("@"))
		    numRecipients++;
		else
		    numWords++;
	    }
	    features.iWords = numWords;
	    features.iHashtags = numHashtags;
	    features.iRecipients = numRecipients;
	    features.bUrl = hasUrl;
	    listOfFeatures.add(features);
	    //System.out.println(features);
	}
	return listOfFeatures;
    } // extractFeaturesFromTuples()

    private List<Datapoint> transformToDatapoints
	(List<Features> listOfFeatures) {
	List<Datapoint> listOfDatapoints = new ArrayList<Datapoint>();
	int counter = 0;
	int informative = 0;
	int non_informative = 0;
	for(Features features : listOfFeatures) {
	    double [] elements = new double[4];
	    elements[0] = features.iWords;
	    elements[1] = features.iHashtags;
	    elements[2] = features.iRecipients;
	    elements[3] = (features.bUrl) ? 1.0 : 0.0;
	    Datapoint datapoint = new Datapoint
		(features.id, elements, features.labelInformative);
	    if(features.labelInformative)
		informative++;
	    else 
		non_informative++;
	    listOfDatapoints.add(datapoint);
	    //if(counter++ == 100) break;
	}
	System.out.println("Informative: " + informative);
	System.out.println("Non-informative: " + non_informative);
	return listOfDatapoints;
    } // transformToDatapoints()
    
    private void saveDatabase(Database database) {
	List<Tuple> tuples = readDatasetFromFile();
	System.out.println(tuples.size());
	for(Tuple tuple : tuples) {
	    System.out.println(tuple);
	    database.insertTuple(tuple); 
	}
    } // saveDatabase()

    private List<Tuple> readDatasetFromFile() {	
	List<Tuple> tuples = new ArrayList<Tuple>();
	//final String FILE_NAME = "labeledWholeDatasetCategories.txt";
	final String FILE_NAME = "labeledWholeDataset.txt";
	//final String FILE_NAME = "draft.txt";
	int counter = 0;
	BufferedReader reader = null;
	try{
	    reader = new BufferedReader(new FileReader(FILE_NAME));
	    String line;
	    while(reader.ready()) {
		line = reader.readLine();
		if(line.equals("")) continue;
		Tuple tuple = new Tuple();
		if(line.equals("[message ID]")) {
		    counter++;
		    tuple.messageID = new BigDecimal(reader.readLine());
		    // "[times favorited]"
		    line = reader.readLine(); 
		    line = reader.readLine(); 
		    // "[times retweeted]"
		    line = reader.readLine(); 
		    line  = reader.readLine(); 
		    // "[author ID]"
		    line = reader.readLine();
		    tuple.authorID = new BigDecimal(reader.readLine());
		    // "[author # of followers]"
		    line = reader.readLine(); 
		    line = reader.readLine();
		    // "[author # of friends]"
		    line = reader.readLine(); 
		    line = reader.readLine();
		    // "[author # of tweets]"
		    line = reader.readLine(); 
		    line = reader.readLine();
		    // "[reply to message ID]"
		    line = reader.readLine();
		    tuple.replyToMessageID = 
			new BigDecimal(reader.readLine());
		    // "[reply to author ID]"
		    line = reader.readLine();
		    tuple.replyToAuthorID = 
			new BigDecimal(reader.readLine());
		    // "[retweet of message ID]"
		    line = reader.readLine();
		    tuple.retweetFromMessageID = 
			new BigDecimal(reader.readLine());
		    // "[retweet to author ID]"
		    line = reader.readLine(); 
		    tuple.retweetFromAuthorID = 
			new BigDecimal(reader.readLine());
		    // "[original message (for retweet) times favorited]"
		    line = reader.readLine(); 
		    line = reader.readLine();
		    // "[original message (for retweet) times retweeted]"
		    line = reader.readLine(); 
		    line = reader.readLine();
		    // "[message]"
		    line = reader.readLine(); 
		    tuple.message = reader.readLine();
		    tuple.message = tuple.message.replace("'","");
		    // "[isReply]"
		    line = reader.readLine();
		    line = reader.readLine(); 
		    // "[isRetweet]"
		    line = reader.readLine();
		    line = reader.readLine(); 
		    // "[outlier]"
		    line = reader.readLine();
		    String outlierLabel = reader.readLine();
		    if(outlierLabel.equals("true")) 
			tuple.informative = false;
		    else 
			tuple.informative = true;
		    // "[category]"		    
		    line = reader.readLine(); 
		    if(line.equals(""))
			tuple.category = -1;
		    else
			tuple.category = 
			    Integer.parseInt(reader.readLine()); 
		}
		tuples.add(tuple);
	    }
	    reader.close();    
	} catch(IOException ex) { 
	    ex.printStackTrace();
	} finally {
	    return tuples;
	}
    } // readDatasetFromFile()

    /**
     * OMG!!! This is a total mess!!!
     * This is fast, dirty coding
     */
    private List<Tuple> readSecondDatasetFromFile() {	
	List<Tuple> tuples = new ArrayList<Tuple>();
	final String FILE_NAME = "tweetsnew10000en.txt";//"tweetsnew4800.txt";
	int counter = 0;
	BufferedReader reader = null;
	try{
	    reader = new BufferedReader(new FileReader(FILE_NAME));
	    String line;
	    int inf = 0;
	    int nonInf = 0; 
	    while(reader.ready()) {
		line = reader.readLine();
		if(line.equals("")) continue;
		Tuple tuple = new Tuple(); 
		if(line.startsWith("T")) {
		    counter++;
		    tuple.messageID = new BigDecimal(counter);
		    line = reader.readLine(); // U
		    line = reader.readLine(); // W
		    tuple.message = line;
		    tuple.message = tuple.message.replace("W\t","");
		    line = reader.readLine(); // O
		    if(line.contains("true")) {
			tuple.informative = false;
			nonInf++;
		    } else {
			inf++;
			tuple.informative = true;
		    }
		}
		tuples.add(tuple);
		//System.out.println(tuple.messageID);
		//System.out.println(tuple.message);
		//System.out.println(tuple.informative);
	    }
	    System.out.println("total: " + (inf + nonInf));
	    System.out.println("informative: " + inf);
	    System.out.println("non-informative: " + nonInf);
	    reader.close();    
	} catch(IOException ex) { 
	    ex.printStackTrace();
	} finally {
	    return tuples;
	}
    } // readSecondDatasetFromFile()

    public static void main(String[] args) {
	ProjectProperties properties = new ProjectProperties();

	properties.project = args[0];
	properties.database = args[1];
	properties.table = args[2];
	String mode = args[3];
	String clusteringChoice = args[4];
	int k = Integer.parseInt(args[5]);
	String classificationChoice = args[6];
	String textClassifierChoice = args[7];
	int cvPartitions = Integer.parseInt(args[8]);
	double weightFreqHashtags = Double.parseDouble(args[9]); 
	double weightFreqWords = Double.parseDouble(args[10]);
	double weightFreqUsers = Double.parseDouble(args[11]);
	double weightSmoothHashtags = Double.parseDouble(args[12]);
	double weightSmoothWords = Double.parseDouble(args[13]);
	double weightSmoothUsers = Double.parseDouble(args[14]);

	//System.out.println("This is project: " + properties.project ); 
	//System.out.println("Database: " + properties.database); 
	//System.out.println("Table: " + properties.table); 
	/*
	weightFreqHashtags = 1.0;
	weightFreqWords = 1.0;
	weightFreqUsers = 1.0;
	weightSmoothUsers = 0.05;
	weightSmoothWords = 0.05;
	weightSmoothHashtags = 0.05;
	while(weightSmoothHashtags < 1.06) {
	    weightSmoothWords = 0.05;
	    while(weightSmoothWords < 1.06) {
		weightSmoothUsers = 0.05;
		while(weightSmoothUsers < 1.06) {*/
	InfoTweets.getInstance().execute
	    (properties, mode, clusteringChoice, k,
	     classificationChoice, textClassifierChoice, cvPartitions,
	     weightFreqHashtags,weightFreqWords,weightFreqUsers,
	     weightSmoothHashtags, weightSmoothWords,weightSmoothUsers);
	/*	weightSmoothUsers += 0.2;
		}
	weightSmoothWords += 0.2;
	    }
	weightSmoothHashtags += 0.2;
	}*/
    }

} // InfoTweets
