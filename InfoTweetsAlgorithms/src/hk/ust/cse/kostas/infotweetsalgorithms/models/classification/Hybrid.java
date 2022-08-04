/*
 * Created on February 23, 2014 by kostas-κγ
 *
 * This is part of the InfoTweetAlgorithms project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetsalgorithms.models.classification;

import java.util.List;  
import java.util.ArrayList;  
import java.util.Set;
import java.util.HashSet;
import java.util.Collections; 

import infotweetsalgorithms.input.document.Document;  
import infotweetsalgorithms.vocabulary.Vocabulary;
//import infotweetsalgorithms.algorithms.tfidf.TfIdf;

/**
 * Hybrid: Binary multinomial Naive Bayes for 
 * text classification with Bayesian Network for features.
 *
 * @author kostas-κγ 
 */
public class Hybrid extends Classifier  {
    
    protected double[] priors;
    protected double[][] conditionalProbabilities; 
    protected Vocabulary vocabulary; 
    //private TfIdf tfidf;

    private int hashtagCeiling; 
    private int wordsCeiling; 
    private int recipientsCeiling; 

    public double[][] wordsLikelihood;
    public double[][] hashtagsLikelihood; 
    public double[][] urlLikelihood;
    public double[][] recipientsLikelihood; 

    private double wordsWeight;
    private double hashtagsWeight;
    private double urlWeight;
    private double recipientsWeight;
    
    public Hybrid(List<Document> documents, int numPartitions) {
	super(documents, numPartitions);
	this.priors = new double[2];
	this.vocabulary = new Vocabulary(); 

	initCeilings();  
	this.wordsLikelihood = new double[this.wordsCeiling][2];
	this.hashtagsLikelihood = new double[this.hashtagCeiling][2]; 
	this.urlLikelihood = new double[2][2];  
	this.recipientsLikelihood = new double[this.recipientsCeiling][2]; 
	
	this.wordsWeight = 1.0;
	this.hashtagsWeight = 1.0;
	this.urlWeight = 1.0;
	this.recipientsWeight = 1.0;

	initArraysForBinaryFeatures();
	initArraysForNumericalFeatures();
    } // Hybrid()

    public void initCeilings() {
	this.wordsCeiling = 40;
	this.hashtagCeiling = 20;
	this.recipientsCeiling = 15;
    } // initCeilings()

    protected void reset(List<Document> trainingDocuments) {  
	this.priors = new double[2];
	this.vocabulary.build(trainingDocuments);
	int vocSize = this.vocabulary.sizeOf(); 
	//this.tfidf = new TfIdf(documents,this.vocabulary);	
	//this.tfidf.estimateTfIdf();

	this.conditionalProbabilities = new double[vocSize][2];

	initCeilings();
	this.wordsLikelihood = new double[this.wordsCeiling][2];
	this.hashtagsLikelihood = new double[this.hashtagCeiling][2];
	this.recipientsLikelihood = new double[this.recipientsCeiling][2];
	this.urlLikelihood = new double[2][2];
	initArraysForBinaryFeatures();
	initArraysForNumericalFeatures();
    } // reset()

    private void initArraysForBinaryFeatures() {
	// priorss
	for (int i=0; i < this.priors.length; i++)
	    this.priors[i] = 0.0;

	// binary features
	for (int i = 0; i < 2; i++) {
	    for (int j = 0; j < 2; j++) {
		this.urlLikelihood[i][j] = 0.0;
		//this.retweetLikelihood[i][j] = 0.0;
	    }
	}
    } // initArraysForBinaryFeatures()

    /**
     *
     */
    public void initArraysForNumericalFeatures() {
	// initiallize arrays with numerical features
	for (int i = 0; i < this.wordsCeiling; i++)
	    for (int j = 0; j < 2; j++)
		this.wordsLikelihood[i][j] = 0.0;

	for (int i = 0; i < this.hashtagCeiling; i++)
	    for (int j = 0; j < 2; j++) 
		this.hashtagsLikelihood[i][j] = 0.0;

	for (int i = 0; i < this.recipientsCeiling; i++)
	    for (int j = 0; j < 2; j++)
		this.recipientsLikelihood[i][j] = 0.0;	    	
    } // initArraysForNumericalFeatures


    @Override protected void train(List<Document> trainingDocuments) { 
	System.out.println("Voc size: " + this.vocabulary.sizeOf());
	int informativesCounter = this.splitter.countInformatives();
	int noninformativesCounter = this.splitter.countNonInformatives();
	int totalNum = informativesCounter + noninformativesCounter;
	double[] N_c = new double[2];
	N_c[0] = 1.0*informativesCounter;
	N_c[1] = 1.0*noninformativesCounter;
	int T = 0;
	int[][] T_c = new int[2][this.vocabulary.sizeOf()];

	for(int c = 0; c < 2; c++){
	    T = 0;
	    this.priors[c] = 1.0 * N_c[c]/totalNum; 
	    List<Integer> textIndexes_c = 
		concatenateTextOfAllDocumentsInClass(c, trainingDocuments);
	    for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		T_c[c][v] = countTokensOfTerm(textIndexes_c, v);
		T += T_c[c][v];
	    }
	    for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		this.conditionalProbabilities[v][c] = 
		    1.0*(T_c[c][v] + 1)/(T + this.vocabulary.sizeOf());
	    }	    

	    for(int i = 0; i < this.wordsCeiling; i++)
		this.wordsLikelihood[i][c] = 
		    (this.wordsLikelihood[i][c] + 1) / (N_c[c] + 2);
	    for(int i = 0; i < this.hashtagCeiling; i++)
		this.hashtagsLikelihood[i][c] = 
		    (this.hashtagsLikelihood[i][c] + 1) / (N_c[c] + 2);
	    for(int i=0; i< 2; i++)
		this.urlLikelihood[i][c] = 
		    (this.urlLikelihood[i][c] + 1) / (N_c[c] + 2);
	    for(int i = 0; i < this.recipientsCeiling; i++)
		this.recipientsLikelihood[i][c] = 
		    (this.recipientsLikelihood[i][c] + 1) / (N_c[c] + 2);

	}
	//promptTrainingVariables();

	for(Document trainingDoc : trainingDocuments) { 
	    //normalized tuple -- feature values -- for each training tweet
	    int [] values = trainingDoc.values;
	    int info = (trainingDoc.labelInformative)? 0 : 1;	    
	    this.wordsLikelihood[values[0]][info]++;
	    this.hashtagsLikelihood[values[1]][info]++;
	    this.urlLikelihood[values[2]][info]++; 
	    this.recipientsLikelihood[values[3]][info]++; 
	} // next

    } // train()

    private void promptTrainingVariables() {
	System.out.println("priors");
	for(int c = 0; c < 2; c++){ 
	    System.out.println(this.priors[c]);
	}
	System.out.println("conditionals");
	for(int v = 0; v < this.vocabulary.sizeOf(); v++){ 
	    System.out.println(v +"\t"+this.vocabulary.getTermByIndex(v));
	    for(int c = 0; c < 2; c++){   
		System.out.println(this.conditionalProbabilities[v][c]);
	    }
	}
    } // promptTrainingVariables();

    /**
     *
     */
    private List<Integer> concatenateTextOfAllDocumentsInClass
	(int c, List<Document> trainingDocuments) {
	List<Integer> textIndexes = new ArrayList<Integer>();
	for(Document trainingDoc : trainingDocuments) {
	    List<String> terms = trainingDoc.tokens;
	    if(c == 0) {
		if(trainingDoc.labelInformative) 
		    for(String term : terms)
			textIndexes.add(this.vocabulary.indexOfTerm(term));
	    } else if(c == 1) {
		if(!trainingDoc.labelInformative) 
		    for(String term : terms)
			textIndexes.add(this.vocabulary.indexOfTerm(term));
	    }
	}
	return textIndexes;
    } // concatenateTextOfAllDocumentsInClass()

    /**
     *
     */
    private int countTokensOfTerm(List<Integer> textIndexes_c, 
				  int vocabularyTermIndex) {
	int occurrences = 0;
	return occurrences = Collections.frequency
	    (textIndexes_c,vocabularyTermIndex);
    } // countTokensOfTerm()


    @Override protected void apply(List<Document> testingDocuments) {
	for(Document doc: testingDocuments) { 
	    Set<Integer> docTokensIndexesInVocabulary =  
		new HashSet<Integer>(); 
	    for(String token : doc.tokens) { 
		int vocIndex = this.vocabulary.indexOfTerm(token);
		if(vocIndex != -1) 
		//if(this.tfidf.getScoreByVocabularyIndex(vocIndex)>20.0)
		    docTokensIndexesInVocabulary.add(vocIndex);
	    }

	    int [] values = doc.values;
	    double[] probability = new double[2]; 

	    double[] score = new double[2];  
	    for(int c = 0; c < 2; c++) {
		score[c] = 1.0 * this.priors[c]; 
		for(int t : docTokensIndexesInVocabulary) { 	  
		    score[c] *= 1.0 * 
			this.conditionalProbabilities[t][c];
		}
		//probability[c] = 1.0 *
		    //this.wordsLikelihood[values[0]][c]  *
		    //this.hashtagsLikelihood[values[1]][c];// *
		    //this.urlLikelihood[values[2]][c];// *
		    //this.recipientsLikelihood[values[3]][c];
		//score[c] *= probability[c];
	    }

	    doc.assignInformative = 
		(score[0] > score[1] ? true : false);
	    //doc.assignProbability = ;
	}
    } // apply()

} // Hybrid